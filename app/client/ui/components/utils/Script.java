package client.ui.components.utils;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;

import client.ClientConf;

public class Script {
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

    public static class Manager {
        public static enum Status {
            NEW,
            LOADING,
            READY
        }

        private String script;
        private Manager base;

        private Status status = Status.NEW;

        public Manager(String script, Manager base) {
            this.script = script;
            this.base = base;
        }

        public Manager(String script) {
            this(script, null);
        }

        public void load(final Runnable callback) {
            switch (status) {
                case NEW:
                    break;

                case LOADING:
                    return;

                case READY:
                    callback.run();
                    return;
            }

            status = Status.LOADING;

            final Callback<Void, Exception> scriptCallback =
                new Callback<Void, Exception>() {
                    @Override
                    public void onFailure(Exception exception) {
                    }

                    @Override
                    public void onSuccess(Void result) {
                        status = Status.READY;

                        callback.run();
                    }
                };

            if (base == null) {
                Script.load(script, scriptCallback);
            } else {
                base.load(new Runnable() {
                    @Override
                    public void run() {
                        Script.load(script, scriptCallback);
                    }
                });
            }
        }

        public Status getStatus() {
            return status;
        }
    }

    public static class JQueryManager extends Manager {
        public static final String SCRIPT =
            ClientConf.asset("js/jquery/jquery-2.1.3.min.js");

        public JQueryManager() {
            super(SCRIPT);
        }

        @Override
        public void load(final Runnable callback) {
            super.load(new Runnable() {
                @Override
                public void run() {
                    ScriptInjector.fromString("jQuery.noConflict();")
                        .setWindow(ScriptInjector.TOP_WINDOW)
                        .inject();

                    callback.run();
                }
            });
        }
    }

    public static Manager JQUERY = new JQueryManager();
}