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

/**
 * Exception during serialization or deserialization of a
 * {@code Serializable} object.
 */
@SuppressWarnings("serial")
public class SerializerException extends Exception {
    /**
     * Kind of serialization error.
     */
    public static enum Error {
        NOT_SERIALIZABLE,
        NOT_DESERIALIZABLE
    }

    /**
     * Initialize {@code SerializerException}.
     *
     * @param error Kind of {@link SerializerException.Error}.
     */
    public SerializerException(Error error) {
        super(getMessage(error));
    }

    /**
     * Initialize {@code SerializerException}.
     *
     * @param error Kind of {@link SerializerException.Error}.
     * @param caught Wrapped {@code Throwable}.
     */
    public SerializerException(Error error, Throwable caught) {
        super(getMessage(error) + " (" + caught.toString() + ")");
    }

    /**
     * Get message describing the kind of error.
     *
     * @param error Kind of error.
     * @return Error description.
     */
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