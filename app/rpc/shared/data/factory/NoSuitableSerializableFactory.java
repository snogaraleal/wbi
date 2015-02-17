package rpc.shared.data.factory;

import rpc.shared.data.Type;

public class NoSuitableSerializableFactory extends Exception {
    private static final long serialVersionUID = -574287770192848989L;

    private static String message = "Serializable factory provider required";

    public NoSuitableSerializableFactory(Type type) {
        super(type.toString());
    }

    public NoSuitableSerializableFactory() {
        super(message);
    }
}
