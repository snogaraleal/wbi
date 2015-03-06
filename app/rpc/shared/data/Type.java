package rpc.shared.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GwtIncompatible;

public class Type {
    private static Map<Class<?>, Type> map = new HashMap<Class<?>, Type>();

    private Class<?> typeClass;
    private Type[] parameterized;

    protected Type(Class<?> typeClass, Type[] parameterized) {
        this.typeClass = typeClass;
        this.parameterized = parameterized;
    }

    public static Type get(Class<?> typeClass, Type... parameterized) {
        if (parameterized == null || parameterized.length == 0) {
            Type type = map.get(typeClass);

            if (type != null) {
                return type;

            } else {
                type = new Type(typeClass, parameterized);
                map.put(typeClass, type);
                return type;
            }
        }

        return new Type(typeClass, parameterized);
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    public Type[] getParameterized() {
        return parameterized;
    }

    public Type getParameterized(int position) {
        return parameterized[position];
    }

    public boolean isBoolean() {
        return typeClass == Boolean.class;
    }

    public boolean isInteger() {
        return typeClass == Integer.class;
    }

    public boolean isLong() {
        return typeClass == Long.class;
    }

    public boolean isFloat() {
        return typeClass == Float.class;
    }

    public boolean isDouble() {
        return typeClass == Double.class;
    }

    public boolean isString() {
        return typeClass == String.class;
    }

    public boolean isEnum() {
        return typeClass.isEnum();
    }

    public boolean isMap() {
        return typeClass == HashMap.class;
    }

    public boolean isList() {
        return typeClass == List.class;
    }

    @GwtIncompatible
    public boolean isInstance(Object object) {
        return typeClass.isInstance(object);
    }

    @Override
    public String toString() {
        if (parameterized == null) {
            return typeClass.toString();
        } else {
            String parameterizedString = "";
            boolean needsComma = false;

            for (Type type : parameterized) {
                if (needsComma) {
                    parameterizedString += ", ";
                } else {
                    needsComma = true;
                }

                parameterizedString += type.toString();
            }

            return typeClass + "<" + parameterizedString + ">";
        }
    }
}
