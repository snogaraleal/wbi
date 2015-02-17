package rpc.shared.data.factory;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

public interface DefaultSerializableFactory {
    public Serializable make(Type type);
}
