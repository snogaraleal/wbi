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

package client.ui.components.utils;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;

import client.ClientConf;
import client.ui.GlobalLoadingIndicator;

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

    public static class Loader {
        public static enum Status {
            NEW,
            LOADING,
            READY
        }

        private String script;
        private Loader base;

        private Status status = Status.NEW;

        public Loader(String script, Loader base) {
            this.script = script;
            this.base = base;
        }

        public Loader(String script) {
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

            GlobalLoadingIndicator.start();

            final Callback<Void, Exception> scriptCallback =
                new Callback<Void, Exception>() {
                    @Override
                    public void onFailure(Exception exception) {
                        GlobalLoadingIndicator.finish();
                    }

                    @Override
                    public void onSuccess(Void result) {
                        GlobalLoadingIndicator.finish();

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

    public static class JQueryLoader extends Loader {
        public static final String SCRIPT =
            ClientConf.asset("js/jquery/jquery-2.1.3.min.js");

        public JQueryLoader() {
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

    public static Loader JQUERY = new JQueryLoader();
}
