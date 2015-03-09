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

package rpc.shared.data;

@SuppressWarnings("serial")
public class SerializerException extends Exception {
    public static enum Error {
        NOT_SERIALIZABLE,
        NOT_DESERIALIZABLE
    }

    public SerializerException(Error error) {
        super(getMessage(error));
    }

    public SerializerException(Error error, Throwable caught) {
        super(getMessage(error) + " (" + caught.toString() + ")");
    }

    public static String getMessage(Error error) {
        switch (error) {
            case NOT_SERIALIZABLE:
                return "Object not serializable";
            case NOT_DESERIALIZABLE:
                return "Object not deserializable";
            default:
                return "Serializer exception";
        }
    }
}
