package rpc.server.invoke;

import java.util.List;
import java.util.ArrayList;

import rpc.shared.data.Serializer;
import rpc.shared.data.SerializerException;
import rpc.shared.data.Type;

public class Invoker {
    private Serializer serializer;

    public Invoker(Serializer serializer) {
        this.serializer = serializer;
    }

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

    public String invoke(
            Invokable invokable,
            List<String> argumentPayloadList)
        throws InvokerException {

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
