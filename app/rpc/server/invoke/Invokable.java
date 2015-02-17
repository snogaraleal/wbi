package rpc.server.invoke;

import java.util.List;

import rpc.shared.data.Type;

public interface Invokable {
    List<Type> getArgumentTypeList() throws Exception;
    Object invoke(Object... arguments) throws Exception;
}
