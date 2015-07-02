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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GwtIncompatible;

/**
 * {@code Type} describing a class that can be parameterized.
 */
public class Type {
    private static Map<Class<?>, Type> map = new HashMap<Class<?>, Type>();

    private Class<?> typeClass;
    private Type[] parameterized;

    /**
     * Initialize {@code Type}.
     *
     * @param typeClass Type class.
     * @param parameterized Parameterized types.
     */
    protected Type(Class<?> typeClass, Type[] parameterized) {
        this.typeClass = typeClass;
        this.parameterized = parameterized;
    }

    /**
     * Get internally-registered type.
     *
     * @param typeClass Type class.
     * @param parameterized Parameterized types.
     * @return {@code Type} instance.
     */
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

    /**
     * Get type class.
     *
     * @return Type class.
     */
    public Class<?> getTypeClass() {
        return typeClass;
    }

    /**
     * Get parameterized types.
     *
     * @return Parameterized types.
     */
    public Type[] getParameterized() {
        return parameterized;
    }

    /**
     * Get parameterized type at the specified position.
     *
     * @param position Position.
     * @return Parameterized type.
     */
    public Type getParameterized(int position) {
        return parameterized[position];
    }

    /**
     * Get whether this type describes a {@code boolean}.
     *
     * @return Whether this type describes a {@code boolean}.
     */
    public boolean isBoolean() {
        return typeClass == Boolean.class;
    }

    /**
     * Get whether this type describes an {@code int}.
     *
     * @return Whether this type describes an {@code int}.
     */
    public boolean isInteger() {
        return typeClass == Integer.class;
    }

    /**
     * Get whether this type describes a {@code long}.
     *
     * @return Whether this type describes a {@code long}.
     */
    public boolean isLong() {
        return typeClass == Long.class;
    }

    /**
     * Get whether this type describes a {@code float}.
     *
     * @return Whether this type describes a {@code float}.
     */
    public boolean isFloat() {
        return typeClass == Float.class;
    }

    /**
     * Get whether this type describes a {@code double}.
     *
     * @return Whether this type describes a {@code double}.
     */
    public boolean isDouble() {
        return typeClass == Double.class;
    }

    /**
     * Get whether this type describes a {@code String}.
     *
     * @return Whether this type is a {@code String}.
     */
    public boolean isString() {
        return typeClass == String.class;
    }

    /**
     * Get whether this type describes an {@code enum} class.
     *
     * @return Whether this type describes an {@code enum} class.
     */
    public boolean isEnum() {
        return typeClass.isEnum();
    }

    /**
     * Get whether this type describes a {@code Map}.
     *
     * @return Whether this type describes a {@code Map}.
     */
    public boolean isMap() {
        return typeClass == HashMap.class;
    }

    /**
     * Get whether this type describes a {@code List}.
     *
     * @return Whether this type describes a {@code List}.
     */
    public boolean isList() {
        return typeClass == List.class;
    }

    /**
     * Get whether the specified object is an instance of this type.
     *
     * This method can only be used when reflection is available, meaning that
     * it is {@code GwtIncompatible}.
     *
     * @param object Object instance.
     * @return Whether the specified object is an instance of this type.
     */
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