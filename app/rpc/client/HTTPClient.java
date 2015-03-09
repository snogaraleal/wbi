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
import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;
import rpc.shared.call.InvalidPayload;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;

public class HTTPClient extends Client implements RequestCallback {
    private RequestBuilder builder;

    private Map<ClientRequest<?>, Request> pendingByClientRequest =
        new HashMap<ClientRequest<?>, Request>();
    private Map<Request, ClientRequest<?>> pendingByRequest =
        new HashMap<Request, ClientRequest<?>>();

    public HTTPClient(Serializer serializer, String url) {
        super(serializer);

        builder = new RequestBuilder(RequestBuilder.POST, url);
    }

    @Override
    public void send(ClientRequest<?> clientRequest) {
        String body;

        try {
            CallRequest callRequest = buildCallRequest(clientRequest);
            body = toRequestMessage(callRequest);
        } catch (SerializerException exception) {
            clientRequest.finish(new ClientRequest.Error(exception));
            return;
        }

        try {
            builder.setHeader("Content-Type", "application/json");
            Request request = builder.sendRequest(body, this);

            pendingByClientRequest.put(clientRequest, request);
            pendingByRequest.put(request, clientRequest);
        } catch (RequestException exception) {
            clientRequest.finish(new ClientRequest.Error(exception));
        }
    }

    @Override
    public void cancel(ClientRequest<?> clientRequest) {
        Request request = pendingByClientRequest.get(clientRequest);

        if (request != null) {
            request.cancel();

            pendingByClientRequest.remove(clientRequest);
            pendingByRequest.remove(request);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onResponseReceived(Request request, Response response) {
        ClientRequest clientRequest = pendingByRequest.get(request);

        pendingByClientRequest.remove(clientRequest);
        pendingByRequest.remove(request);

        if (response.getStatusCode() == Response.SC_OK) {
            try {
                CallResponse callResponse =
                    fromResponseMessage(response.getText());

                String payload = callResponse.getPayload();

                if (callResponse.isSuccess()) {
                    clientRequest.finish(
                        deserialize(payload, clientRequest.getExpected()));
                } else {
                    clientRequest.finish(new ClientRequest.Error(payload));
                }
            } catch (InvalidPayload exception) {
                clientRequest.finish(new ClientRequest.Error(exception));

            } catch (SerializerException exception) {
                clientRequest.finish(new ClientRequest.Error(exception));
            }
        } else {
            String reason = response.getStatusText();
            clientRequest.finish(new ClientRequest.Error(reason));
        }
    };

    @Override
    public void onError(Request request, Throwable exception) {
        ClientRequest<?> clientRequest = pendingByRequest.get(request);

        pendingByClientRequest.remove(clientRequest);
        pendingByRequest.remove(request);

        clientRequest.finish(new ClientRequest.Error(exception));
    };
}
