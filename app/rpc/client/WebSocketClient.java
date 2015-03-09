/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package rpc.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rpc.client.websocket.WebSocket;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;

public class WebSocketClient extends Client implements WebSocket.Listener {
    private WebSocket socket;

    private Map<ClientRequest<?>, CallRequest> waitingForSocket =
        new HashMap<ClientRequest<?>, CallRequest>();
    private Map<String, ClientRequest<?>> pendingByToken =
        new HashMap<String, ClientRequest<?>>();

    public WebSocketClient(Serializer serializer, String url) {
        super(serializer);

        socket = WebSocket.create(url);
        socket.addListener(this);
    }

    public static boolean isSupported() {
        return WebSocket.isSupported();
    }

    @Override
    public void send(ClientRequest<?> clientRequest) {
        try {
            CallRequest callRequest = waitingForSocket.get(clientRequest);
            if (callRequest == null) {
                callRequest = buildCallRequest(clientRequest);
                pendingByToken.put(callRequest.getToken(), clientRequest);
            }

            try {
                socket.send(toRequestMessage(callRequest));
                waitingForSocket.remove(clientRequest);
            } catch (Exception exception) {
                waitingForSocket.put(clientRequest, callRequest);
            }
        } catch (SerializerException exception) {
            clientRequest.finish(new ClientRequest.Error(exception));
        }
    }

    @Override
    public void cancel(ClientRequest<?> clientRequest) {
        Iterator<Map.Entry<String, ClientRequest<?>>> iterator =
            pendingByToken.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, ClientRequest<?>> entry = iterator.next();
            if (entry.getValue() == clientRequest) {
                iterator.remove();
            }
        }
    }

    @Override
    public void onOpen(WebSocket socket) {
        for (ClientRequest<?> clientRequest : waitingForSocket.keySet()) {
            send(clientRequest);
        }

        waitingForSocket.clear();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onMessage(WebSocket socket, String message) {
        CallResponse callResponse;

        try {
            callResponse = fromResponseMessage(message);
        } catch (InvalidPayload exception) {
            return;
        }

        String token = callResponse.getToken();
        String payload = callResponse.getPayload();
        ClientRequest clientRequest = pendingByToken.get(token);

        if (clientRequest == null) {
            return;
        }

        try {
            if (callResponse.isSuccess()) {
                clientRequest.finish(
                    deserialize(payload, clientRequest.getExpected()));
            } else {
                clientRequest.finish(new ClientRequest.Error(payload));
            }
        } catch (SerializerException exception) {
            clientRequest.finish(new ClientRequest.Error(exception));
        }

        pendingByToken.remove(token);
    }

    @Override
    public void onError(WebSocket socket, String reason) {
        for (ClientRequest<?> clientRequest : pendingByToken.values()) {
            clientRequest.finish(new ClientRequest.Error(reason));
        }

        pendingByToken.clear();
    }

    @Override
    public void onClose(WebSocket socket, String reason, boolean clean) {
        for (ClientRequest<?> clientRequest : pendingByToken.values()) {
            clientRequest.finish(new ClientRequest.Error(reason));
        }

        pendingByToken.clear();
    }
}
