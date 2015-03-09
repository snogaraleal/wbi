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

@SuppressWarnings("serial")
public class InvokerException extends Exception {
    private static String GENERIC_MESSAGE = "Exception during invocation";

    public static enum Reason {
        INCOMPATIBLE_ARGUMENTS,
        INVALID_PAYLOAD,
        INVOKABLE_EXCEPTION,
        UNSUPPORTED_RETURN_VALUE
    }

    public InvokerException(Reason reason) {
        super(getMessage(reason));
    }

    public InvokerException(Reason reason, Throwable caught) {
        super(getMessage(reason) + " (" + caught + ")");
    }

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
