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
 * {@code Serializer} capable of serializing and deserializing
 * {@link Serializable} objects.
 */
public interface Serializer {
    /**
     * Serialize the specified object.
     *
     * @param object Object to serialize.
     * @return Serialized payload.
     * @throws SerializerException
     */
    String serialize(Object object)
        throws SerializerException;

    /**
     * Deserialize the specified payload.
     *
     * @param payload Payload to deserialize.
     * @param expected Expected object type.
     * @return Deserialized object.
     * @throws SerializerException
     */
    Object deserialize(String payload, Type expected)
        throws SerializerException;
}