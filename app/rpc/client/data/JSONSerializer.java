package rpc.client.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

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

    private JSONValue toJSONValue(Object object) {
        // Null
        if (object == null) {
            return JSONNull.getInstance();
        }

        // Boolean
        Boolean asBoolean = Utils.isBoolean(object);
        if (asBoolean != null) {
            return JSONBoolean.getInstance(asBoolean);
        }

        // Integer
        Integer asInteger = Utils.isInteger(object);
        if (asInteger != null) {
            return new JSONNumber(asInteger);
        }

        // Long
        Long asLong = Utils.isLong(object);
        if (asLong != null) {
            return new JSONNumber(asLong);
        }

        // Float
        Float asFloat = Utils.isFloat(object);
        if (asFloat != null) {
            return new JSONNumber(asFloat);
        }

        // Double
        Double asDouble = Utils.isDouble(object);
        if (asDouble != null) {
            return new JSONNumber(asDouble);
        }

        // String
        String asString = Utils.isString(object);
        if (asString != null) {
            return new JSONString(asString);
        }

        // Enum
        Enum asEnum = Utils.isEnum(object);
        if (asEnum != null) {
            return new JSONString(asEnum.toString());
        }

        // Serializable
        Serializable asSerializable = Utils.isSerializable(object);
        if (asSerializable != null) {
            JSONObject jsonObject = new JSONObject();

            for (String field : asSerializable.fields().keySet()) {
                Object value = asSerializable.get(field);
                jsonObject.put(field, toJSONValue(value));
            }

            return jsonObject;
        }

        // List
        List<Object> asSerializableList = Utils.isSerializableList(object);
        if (asSerializableList != null) {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < asSerializableList.size(); i++) {
                Object item = asSerializableList.get(i);
                jsonArray.set(i, toJSONValue(item));
            }

            return jsonArray;
        }

        // Map
        Map<Object, Object> asSerializableMap =
            Utils.isSerializableMap(object);
        if (asSerializableMap != null) {
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<Object, Object> entry :
                    asSerializableMap.entrySet()) {

                String key = (String) entry.getKey();
                Object value = entry.getValue();

                jsonObject.put(key, toJSONValue(value));
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

        JSONValue jsonValue = toJSONValue(object);

        if (jsonValue == null) {
            throw new SerializerException(
                SerializerException.Error.NOT_SERIALIZABLE);
        }

        return jsonValue.toString();
    }

    private Object fromJSONValue(JSONValue jsonValue, Type expected)
        throws NoSuitableSerializableFactory {

        if (jsonValue == null) {
            return null;
        }

        // Null
        JSONNull asNull = jsonValue.isNull();
        if (asNull != null) {
            return null;
        }

        // Boolean
        JSONBoolean asBoolean = jsonValue.isBoolean();
        if (asBoolean != null) {
            return asBoolean.booleanValue();
        }

        // Integer
        // Long
        // Float
        // Double
        JSONNumber asNumber = jsonValue.isNumber();
        if (asNumber != null) {
            double value = asNumber.doubleValue();

            if (expected.isInteger()) {
                return (int) value;
            }

            if (expected.isLong()) {
                return (long) value;
            }

            if (expected.isFloat()) {
                return (float) value;
            }

            if (expected.isDouble()) {
                return value;
            }
        }

        // String
        // Enum
        JSONString asString = jsonValue.isString();
        if (asString != null) {
            if (expected.isEnum()) {
                String value = asString.stringValue();
                return Enum.valueOf(
                    (Class) expected.getTypeClass(), value);
            } else {
                return asString.stringValue();
            }
        }

        // Map
        // Serializable
        JSONObject asObject = jsonValue.isObject();
        if (asObject != null) {
            if (expected.isMap()) {
                Map<String, Object> map = new HashMap<String, Object>();

                Type keyType = expected.getParameterized(0);
                Type valueType = expected.getParameterized(1);

                if (!keyType.isString()) {
                    return null;
                }

                for (String key : asObject.keySet()) {
                    JSONValue value = asObject.get(key);
                    map.put(key, fromJSONValue(value, valueType));
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

                    JSONValue value = asObject.get(field);
                    object.set(field, fromJSONValue(value, fieldType));
                }

                return object;
            }
        }

        // List
        JSONArray asArray = jsonValue.isArray();
        if (asArray != null) {
            int size = asArray.size();

            List<Object> list = new ArrayList<Object>();
            Type itemType = expected.getParameterized(0);

            for (int i = 0; i < size; i++) {
                JSONValue value = asArray.get(i);
                list.add(fromJSONValue(value, itemType));
            }

            return list;
        }

        return null;
    }

    @Override
    public Object deserialize(String payload, Type expected)
        throws SerializerException {

        JSONValue jsonValue = JSONParser.parseStrict(payload);

        try {
            Object object = fromJSONValue(jsonValue, expected);

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
