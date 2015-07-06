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

package rpc.server.invoke;

/**
 * Exception during {@code Invokable} call.
 */
@SuppressWarnings("serial")
public class InvokerException extends Exception {
    private static String GENERIC_MESSAGE = "Exception during invocation";

    /**
     * Exception reason.
     */
    public static enum Reason {
        INCOMPATIBLE_ARGUMENTS,
        INVALID_PAYLOAD,
        INVOKABLE_EXCEPTION,
        UNSUPPORTED_RETURN_VALUE
    }

    /**
     * Initialize {@code InvokerException} with a {@link Reason}.
     *
     * @param reason Reason.
     */
    public InvokerException(Reason reason) {
        super(getMessage(reason));
    }

    /**
     * Initialize {@code InvokerException} with a {@link Reason} and a
     * {@code Throwable} providing more details.
     *
     * @param reason Reason.
     * @param caught Throwable.
     */
    public InvokerException(Reason reason, Throwable caught) {
        super(getMessage(reason) + " (" + caught + ")");
    }

    /**
     * Get reason message.
     *
     * @param reason Reason.
     * @return Message.
     */
    private static String getMessage(Reason reason) {
        switch (reason) {
            case INCOMPATIBLE_ARGUMENTS:
                return "Incompatible arguments";

            case INVALID_PAYLOAD:
                return "Invalid payload";

            case INVOKABLE_EXCEPTION:
                return "Exception in invokable";

            case UNSUPPORTED_RETURN_VALUE:
                return "Unsupported return value";

            default:
                return GENERIC_MESSAGE;
        }
    }
}