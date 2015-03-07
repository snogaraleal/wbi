package rpc.shared.data.factory;

import rpc.shared.data.Type;

@SuppressWarnings("serial")
public class NoSuitableSerializableFactory extends Exception {
    private static String message = "Serializable factory provider required";

    public NoSuitableSerializableFactory(Type type) {
        super(type.toString());
    }

    public NoSuitableSerializableFactory() {
        super(message);
    }
}
