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

package rpc.server.invoke;

import java.util.ArrayList;
import java.util.List;

import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.data.Type;

/**
 * Layer for calling an {@link Invokable} with serialized values.
 */
public class Invoker {
    private Serializer serializer;

    /**
     * Initialize {@code Invoker}.
     *
     * @param serializer Serializer.
     */
    public Invoker(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Get required argument types for calling an {@code Invokable} or raise
     * an {@link InvokerException} if not possible.
     *
     * @param invokable Invokable.
     * @return List of argument types.
     * @throws InvokerException
     */
    private List<Type> getArgumentTypeList(Invokable invokable)
        throws InvokerException {

        List<Type> argumentTypeList;

        try {
            argumentTypeList = invokable.getArgumentTypeList();
        } catch (Exception exception) {
            throw new InvokerException(
                InvokerException.Reason.INVOKABLE_EXCEPTION, exception);
        }

        return argumentTypeList;
    }

    /**
     * Deserialize arguments for calling an {@code Invokable} or raise
     * an {@link InvokerException} if not possible.
     *
     * @param argumentPayloadList Serialized arguments.
     * @param argumentTypeList Required argument types.
     * @return Deserialized arguments.
     * @throws InvokerException
     */
    private List<Object> getArgumentList(
            List<String> argumentPayloadList,
            List<Type> argumentTypeList) throws InvokerException {

        if (argumentTypeList.size() != argumentPayloadList.size()) {
            throw new InvokerException(
                InvokerException.Reason.INCOMPATIBLE_ARGUMENTS);
        }

        List<Object> argumentList = new ArrayList<Object>();

        int n = argumentTypeList.size();

        for (int i = 0; i < n; i++) {
            Type expected = argumentTypeList.get(i);
            String payload = argumentPayloadList.get(i);

            try {
                Object object = serializer.deserialize(payload, expected);

                if (!expected.isInstance(object)) {
                    throw new InvokerException(
                        InvokerException.Reason.INCOMPATIBLE_ARGUMENTS);
                }

                argumentList.add(object);

            } catch (SerializerException exception) {
                throw new InvokerException(
                    InvokerException.Reason.INVALID_PAYLOAD, exception);
            }
        }

        return argumentList;
    }

    /**
     * Invoke an {@link Invokable}.
     *
     * @param invokable Invokable to invoke.
     * @param argumentPayloadList Serialized arguments.
     * @return Serialized return value.
     * @throws InvokerException
     */
    public String invoke(
            Invokable invokable,
            List<String> argumentPayloadList) throws InvokerException {

        List<Object> argumentList = getArgumentList(
            argumentPayloadList, getArgumentTypeList(invokable));

        Object returnValue;
        Object[] arguments = argumentList.toArray(
            new Object[argumentList.size()]);

        try {
            returnValue = invokable.invoke(arguments);
        } catch (Exception exception) {
            exception.printStackTrace();

            throw new InvokerException(
                InvokerException.Reason.INVOKABLE_EXCEPTION, exception);
        }

        try {
            return serializer.serialize(returnValue);
        } catch (SerializerException exception) {
            throw new InvokerException(
                InvokerException.Reason.UNSUPPORTED_RETURN_VALUE, exception);
        }
    }
}