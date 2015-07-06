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

package rpc.server.registry;

import rpc.server.Service;

/**
 * {@link Registry} exception.
 */
@SuppressWarnings("serial")
public class RegistryException extends Exception {
    /**
     * Exception reason.
     */
    public static enum Reason {
        SERVICE_CLASS_NOT_FOUND,
        SERVICE_CLASS_NOT_ENABLED,
        SERVICE_METHOD_NOT_FOUND,
        SERVICE_METHOD_NOT_STATIC,
        SERVICE_METHOD_NOT_PUBLIC
    }

    /**
     * Initialize {@code RegistryException}.
     *
     * @param registryService {@link RegistryService}.
     * @param reason Reason.
     */
    public RegistryException(
            RegistryService registryService,
            Reason reason) {

        super(getMessage(registryService, null, reason));
    }

    /**
     * Initialize {@code RegistryException}.
     *
     * @param registryServiceMethod {@link RegistryServiceMethod}.
     * @param reason {@link RegistryException.Reason}.
     */
    public RegistryException(
            RegistryServiceMethod registryServiceMethod,
            Reason reason) {

        super(getMessage(
            registryServiceMethod.getRegistryService(),
            registryServiceMethod, reason));
    }

    /**
     * Initialize {@code RegistryException}.
     *
     * @param registryServiceMethod {@link RegistryServiceMethod}.
     * @param reason {@link RegistryException.Reason}.
     * @param caught Throwable.
     */
    public RegistryException(
            RegistryServiceMethod registryServiceMethod,
            Reason reason,
            Throwable caught) {

        super(getMessage(
            registryServiceMethod.getRegistryService(),
            registryServiceMethod, reason) + " " +
                "(" + caught.toString() + ")");
    }

    /**
     * Initialize {@code RegistryException}.
     *
     * @param registryService {@link RegistryService}.
     * @param reason {@link RegistryException.Reason}.
     * @param caught Throwable.
     */
    public RegistryException(
            RegistryService registryService,
            Reason reason,
            Throwable caught) {

        super(getMessage(registryService, null, reason) + " " +
                "(" + caught.toString() + ")");
    }

    private static String GENERIC_MESSAGE = "Registry exception";

    /**
     * Get message.
     *
     * @param registryService {@link RegistryService}.
     * @param registryServiceMethod {@link RegistryServiceMethod}.
     * @param reason {@link RegistryException.Reason}.
     * @return Message.
     */
    private static String getMessage(
            RegistryService registryService,
            RegistryServiceMethod registryServiceMethod,
            Reason reason) {

        if (reason == null) {
            return GENERIC_MESSAGE;
        }

        switch (reason) {
            case SERVICE_CLASS_NOT_FOUND:
                return
                    "Service class" + 
                    " '" + registryService.getServiceClassName() + "' " +
                    "not found";

            case SERVICE_CLASS_NOT_ENABLED:
                return
                    "Service class" +
                    " '" + registryService.getServiceClassName() + "' " +
                    "must be enabled by implementing " + Service.class;

            case SERVICE_METHOD_NOT_FOUND:
                return
                    "Method" +
                    " '" + registryServiceMethod.getServiceMethodName() + "' " +
                    "not found in service class" +
                    " '" + registryService.getServiceClassName() + "'";

            case SERVICE_METHOD_NOT_STATIC:
                return
                    "Method" +
                    " '" + registryServiceMethod.getServiceMethodName() + "' " +
                    "defined in service class" +
                    " '" + registryService.getServiceClassName() + "' " +
                    "must be static";

            case SERVICE_METHOD_NOT_PUBLIC:
                return
                    "Method" +
                    " '" + registryServiceMethod.getServiceMethodName() + "' " +
                    "defined in service class" +
                    " '" + registryService.getServiceClassName() + "' " +
                    "is not public";

            default:
                return GENERIC_MESSAGE;
        }
    }
}