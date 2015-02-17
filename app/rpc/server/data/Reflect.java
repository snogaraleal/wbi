package rpc.server.data;

import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import rpc.shared.data.Type;

public class Reflect {
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

    public static List<Type> getArgumentTypeList(Method method) {
        List<Type> argumentTypeList = new ArrayList<Type>();

        for (java.lang.reflect.Type reflectType :
                method.getGenericParameterTypes()) {

            argumentTypeList.add(getType(reflectType));
        }

        return argumentTypeList;
    }
}
