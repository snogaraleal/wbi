package client.ui.components.utils;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;

import client.ClientConf;

public class Script {
    private static final String JQUERY =
        ClientConf.asset("jquery/jquery-2.1.3.min.js");

    private static Set<String> loadedScripts = new HashSet<String>();

    public static void load(
            String script, Callback<Void, Exception> callback) {

        if (loadedScripts.contains(script)) {
            callback.onSuccess(null);
            return;
        }

        ScriptInjector.fromUrl(script)
            .setWindow(ScriptInjector.TOP_WINDOW)
            .setCallback(callback)
            .inject();

        loadedScripts.add(script);
    }

    public static void loadWithJQuery(
            final String script, final Callback<Void, Exception> callback) {

        load(JQUERY, new Callback<Void, Exception>() {
            @Override
            public void onFailure(Exception exception) {
            }

            @Override
            public void onSuccess(Void result) {
                ScriptInjector.fromString("jQuery.noConflict();")
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();

                load(script, callback);
            }
        });
    }
}
