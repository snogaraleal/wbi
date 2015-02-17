package rpc.shared.data;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Utils {
    public static Boolean isBoolean(Object object) {
        try {
            return (Boolean) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Integer isInteger(Object object) {
        try {
            return (Integer) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Long isLong(Object object) {
        try {
            return (Long) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Float isFloat(Object object) {
        try {
            return (Float) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Double isDouble(Object object) {
        try {
            return (Double) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static String isString(Object object) {
        try {
            return (String) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Enum isEnum(Object object) {
        try {
            return (Enum) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Serializable isSerializable(Object object) {
        try {
            Serializable asSerializable = (Serializable) object;

            if (asSerializable != null && asSerializable.fields() != null) {
                return asSerializable;
            } else {
                return null;
            }
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static boolean isSerializableValue(Object object) {
        return
            object == null ||
            isBoolean(object) != null ||
            isInteger(object) != null ||
            isLong(object) != null ||
            isFloat(object) != null ||
            isDouble(object) != null ||
            isString(object) != null ||
            isEnum(object) != null ||
            isSerializable(object) != null;
    }

    public static List<Object> isList(Object object) {
        try {
            return (List<Object>) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static List<Object> isSerializableList(Object object) {
        List<Object> list = isList(object);

        if (list != null) {
            if (list.isEmpty()) {
                return list;
            } else {
                if (isSerializableValue(list.get(0))) {
                    return list;
                }
            }
        }

        return null;
    }

    public static Map<Object, Object> isMap(Object object) {
        try {
            return (Map<Object, Object>) object;
        } catch (ClassCastException exception) {
            return null;
        }
    }

    public static Map<Object, Object> isSerializableMap(Object object) {
        Map<Object, Object> map = isMap(object);

        if (map != null) {
            if (map.isEmpty()) {
                return map;
            } else {
                if (isString(map.keySet().iterator().next()) != null &&
                        isSerializableValue(map.values().iterator().next())) {
                    return map;
                }
            }
        }

        return null;
    }

    public static boolean isSerializationCapable(Object object) {
        return
            isSerializableValue(object) ||
            isSerializableList(object) != null ||
            isSerializableMap(object) != null;
    }
}
