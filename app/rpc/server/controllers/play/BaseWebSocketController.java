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

package rpc.server.controllers.play;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import play.libs.F;
import play.mvc.WebSocket;

import rpc.server.GlobalHandler;
import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;

public class BaseWebSocketController extends BaseController {
    public static class WebSocketActor extends UntypedActor {
        public static Props props(ActorRef out) {
            return Props.create(WebSocketActor.class, out);
        }

        private final ActorRef out;

        public WebSocketActor(ActorRef out) {
            this.out = out;
        }

        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof String) {
                CallRequest request =
                    requestSerializer.deserialize((String) message);
                CallResponse response = GlobalHandler.handle(request);

                String payload = responseSerializer.serialize(response);

                out.tell(payload, self());
            }
        }
    }

    public static WebSocket<String> socket() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            @Override
            public Props apply(ActorRef out) throws Throwable {
                return WebSocketActor.props(out);
            }
        });
    }
}
