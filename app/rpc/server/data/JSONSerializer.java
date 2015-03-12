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

package rpc.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import rpc.shared.data.Serializable;
import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.data.Type;
import rpc.shared.data.Utils;
import rpc.shared.data.factory.NoSuitableSerializableFactory;
import rpc.shared.data.factory.SerializableFactoryProvider;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JSONSerializer implements Serializer {
    private SerializableFactoryProvider provider;

    public JSONSerializer() {
    }

    public JSONSerializer(SerializableFactoryProvider provider) {
        this.provider = provider;
    }

    private JsonElement toJsonElement(Object object) {
        if (object == null) {
            return JsonNull.INSTANCE;
        }

        // Boolean
        Boolean asBoolean = Utils.isBoolean(object);
        if (asBoolean != null) {
            return new JsonPrimitive(asBoolean);
        }

        // Integer
        Integer asInteger = Utils.isInteger(object);
        if (asInteger != null) {
            return new JsonPrimitive(asInteger);
        }

        // Long
        Long asLong = Utils.isLong(object);
        if (asLong != null) {
            return new JsonPrimitive(asLong);
        }

        // Float
        Float asFloat = Utils.isFloat(object);
        if (asFloat != null) {
            return new JsonPrimitive(asFloat);
        }

        // Double
        Double asDouble = Utils.isDouble(object);
        if (asDouble != null) {
            return new JsonPrimitive(asDouble);
        }

        // String
        String asString = Utils.isString(object);
        if (asString != null) {
            return new JsonPrimitive(asString);
        }

        // Enum
        Enum asEnum = Utils.isEnum(object);
        if (asEnum != null) {
            return new JsonPrimitive(asEnum.toString());
        }

        // Serializable
        Serializable asSerializable = Utils.isSerializable(object);
        if (asSerializable != null) {
            JsonObject jsonObject = new JsonObject();

            for (String field : asSerializable.fields().keySet()) {
                Object value = asSerializable.get(field);
                jsonObject.add(field, toJsonElement(value));
            }

            return jsonObject;
        }

        // List
        List<Object> asSerializableList = Utils.isSerializableList(object);
        if (asSerializableList != null) {
            JsonArray jsonArray = new JsonArray();

            for (Object item : asSerializableList) {
                jsonArray.add(toJsonElement(item));
            }

            return jsonArray;
        }

        // Map
        Map<Object, Object> asSerializableMap =
            Utils.isSerializableMap(object);
        if (asSerializableMap != null) {
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<Object, Object> entry :
                    asSerializableMap.entrySet()) {

                Object keyObject = entry.getKey();
                Object valueObject = entry.getValue();

                String key = null;

                String keyAsString = Utils.isString(keyObject);
                if (keyAsString != null) {
                    key = keyAsString;
                }

                Enum keyAsEnum = Utils.isEnum(keyObject);
                if (keyAsEnum != null) {
                    key = keyAsEnum.toString();
                }

                jsonObject.add(key, toJsonElement(valueObject));
            }

            return jsonObject;
        }

        return null;
    }

    @Override
    public String serialize(Object object) throws SerializerException {
        if (!Utils.isSerializationCapable(object)) {
            throw new SerializerException(
                SerializerException.Error.NOT_SERIALIZABLE);
        }

        JsonElement jsonElement = toJsonElement(object);

        if (jsonElement == null) {
            throw new SerializerException(
                SerializerException.Error.NOT_SERIALIZABLE);
        }

        return jsonElement.toString();
    }

    private Object fromJsonElement(JsonElement jsonElement, Type expected)
        throws NoSuitableSerializableFactory {

        if (jsonElement == null) {
            return null;
        }

        // Null
        if (jsonElement.isJsonNull()) {
            return null;
        }

        // Boolean
        // Integer
        // Long
        // Float
        // Double
        // String
        // Enum
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();

            if (asJsonPrimitive.isBoolean()) {
                return asJsonPrimitive.getAsBoolean();
            }

            if (asJsonPrimitive.isNumber()) {
                if (expected.isInteger()) {
                    return asJsonPrimitive.getAsInt();
                }

                if (expected.isLong()) {
                    return asJsonPrimitive.getAsLong();
                }

                if (expected.isFloat()) {
                    return asJsonPrimitive.getAsFloat();
                }

                if (expected.isDouble()) {
                    return asJsonPrimitive.getAsDouble();
                }

                return asJsonPrimitive.getAsNumber();
            }

            if (asJsonPrimitive.isString()) {
                if (expected.isEnum()) {
                    String value = asJsonPrimitive.getAsString();
                    return Enum.valueOf(
                        (Class) expected.getTypeClass(), value);
                } else {
                    return asJsonPrimitive.getAsString();
                }
            }
        }

        // Map
        // Serializable
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();

            if (expected.isMap()) {
                Map<Object, Object> map = new HashMap<Object, Object>();

                Type keyType = expected.getParameterized(0);
                Type valueType = expected.getParameterized(1);

                if (!(keyType.isString() || keyType.isEnum())) {
                    return null;
                }

                for (Map.Entry<String, JsonElement> entry :
                        asJsonObject.entrySet()) {

                    String key = entry.getKey();
                    JsonElement value = entry.getValue();

                    if (keyType.isString()) {
                        map.put(
                            entry.getKey(),
                            fromJsonElement(value, valueType));
                    }

                    if (keyType.isEnum()) {
                        map.put(
                            Enum.valueOf((Class) keyType.getTypeClass(), key),
                            fromJsonElement(value, valueType));
                    }
                }

                return map;
            } else {
                if (provider == null) {
                    throw new NoSuitableSerializableFactory();
                }

                Serializable object = provider.make(expected);

                for (Map.Entry<String, Type> entry :
                        object.fields().entrySet()) {

                    String field = entry.getKey();
                    Type fieldType = entry.getValue();

                    JsonElement value = asJsonObject.get(field);
                    object.set(field, fromJsonElement(value, fieldType));
                }

                return object;
            }
        }

        // List
        if (jsonElement.isJsonArray()) {
            JsonArray asJsonArray = jsonElement.getAsJsonArray();

            int size = asJsonArray.size();

            List<Object> list = new ArrayList<Object>();
            Type itemType = expected.getParameterized(0);

            for (int i = 0; i < size; i++) {
                JsonElement value = asJsonArray.get(i);
                list.add(fromJsonElement(value, itemType));
            }

            return list;
        }

        return null;
    }

    private static JsonParser parser = new JsonParser();

    @Override
    public Object deserialize(String payload, Type expected)
        throws SerializerException {

        JsonElement jsonElement = parser.parse(payload);

        try {
            Object object = fromJsonElement(jsonElement, expected);

            if (object == null) {
                throw new SerializerException(
                    SerializerException.Error.NOT_DESERIALIZABLE);
            }

            return object;

        } catch (NoSuitableSerializableFactory exception) {
            throw new SerializerException(
                SerializerException.Error.NOT_DESERIALIZABLE, exception);
        }
    }
}
