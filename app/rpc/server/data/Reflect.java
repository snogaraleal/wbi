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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import rpc.shared.data.Type;

/**
 * Reflection utilities.
 */
public class Reflect {
    /**
     * Get {@link Type} from the specified {@link java.lang.reflect.Type}.
     *
     * @param reflectType Reflection type.
     * @return RPC type.
     */
    public static Type getType(java.lang.reflect.Type reflectType) {
        Type[] parameterized = null;

        if (reflectType instanceof ParameterizedType) {
            ParameterizedType reflectParameterizedType =
                (ParameterizedType) reflectType;

            java.lang.reflect.Type[] reflectParameterizedTypes =
                reflectParameterizedType.getActualTypeArguments();

            parameterized = new Type[reflectParameterizedTypes.length];

            for (int i = 0; i < reflectParameterizedTypes.length; i++) {
                parameterized[i] = getType(reflectParameterizedTypes[i]);
            }

            reflectType = reflectParameterizedType.getRawType();
        }

        return Type.get((Class<?>) reflectType, parameterized);
    }

    /**
     * Get a list containing a {@link Type} for each argument of the specified
     * method obtained via reflection.
     *
     * @param method Method.
     * @return List of argument types.
     */
    public static List<Type> getArgumentTypeList(Method method) {
        List<Type> argumentTypeList = new ArrayList<Type>();

        for (java.lang.reflect.Type reflectType :
                method.getGenericParameterTypes()) {

            argumentTypeList.add(getType(reflectType));
        }

        return argumentTypeList;
    }
}