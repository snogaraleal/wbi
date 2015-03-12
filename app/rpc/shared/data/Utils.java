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
                Object mapKey = map.keySet().iterator().next();
                Object mapValue = map.values().iterator().next();

                if ((isString(mapKey) != null || isEnum(mapKey) != null) &&
                        isSerializableValue(mapValue)) {
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
