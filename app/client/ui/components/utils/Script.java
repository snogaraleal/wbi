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

/**
 * Script utilities.
 */
public class Script {
    /**
     * Set of paths to loaded script files.
     */
    private static Set<String> loadedScripts = new HashSet<String>();

    /**
     * Load the specified script.
     *
     * @param script Path to script.
     * @param callback {@code Callback} called when ready.
     */
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

    /**
     * Script loader.
     */
    public static class Loader {
        /**
         * Status of a {@link Loader}.
         */
        public static enum Status {
            NEW,
            LOADING,
            READY
        }

        /**
         * Path to script.
         */
        private String script;

        /**
         * Required script.
         */
        private Loader base;

        /**
         * Current loader {@link Status}.
         */
        private Status status = Status.NEW;

        /**
         * Initialize {@code Loader}.
         *
         * @param script Path to script.
         * @param base Required script.
         */
        public Loader(String script, Loader base) {
            this.script = script;
            this.base = base;
        }

        /**
         * Initialize {@code Loader}.
         *
         * @param script Path to script.
         */
        public Loader(String script) {
            this(script, null);
        }

        /**
         * Load script.
         *
         * @param callback {@code Runnable} called when ready.
         */
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

        /**
         * Get the current {@link Status} of the loader.
         *
         * @return Current status.
         */
        public Status getStatus() {
            return status;
        }
    }

    /**
     * Non-conflicting JQuery loader.
     */
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

    /**
     * Global JQuery loader.
     */
    public static Loader JQUERY = new JQueryLoader();
}
