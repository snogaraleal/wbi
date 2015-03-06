package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import rpc.shared.data.SerializerException;

import views.html.client;

import client.ClientConf;

public class Application extends Controller {
    private static String clientHead;

    private static String getClientHead() throws SerializerException {
        if (clientHead == null) {
            String clientConfJSON = ServerConf.defaultSerializer.serialize(
                ServerConf.getClientConf(request()));
            clientHead = ClientConf.HEAD_ATTR + "=" +
                "'" + clientConfJSON + "'";
        }

        return clientHead;
    }

    public static Result index() throws SerializerException {
        return ok(client.render(getClientHead()));
    }
}
