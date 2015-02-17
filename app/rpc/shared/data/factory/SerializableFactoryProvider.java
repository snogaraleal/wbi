package rpc.shared.data.factory;

import java.util.Map;
import java.util.HashMap;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

public class SerializableFactoryProvider {
    private DefaultSerializableFactory defaultFactory;
    private Map<Type, SerializableFactory> factoryMap =
        new HashMap<Type, SerializableFactory>();

    public SerializableFactoryProvider() {
    }

    public SerializableFactoryProvider(
            DefaultSerializableFactory defaultFactory) {
        this();
        setDefaultFactory(defaultFactory);
    }

    public void addFactory(Type type, SerializableFactory factory) {
        factoryMap.put(type, factory);
    }

    public void setDefaultFactory(DefaultSerializableFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public Serializable make(Type type) throws NoSuitableSerializableFactory {
        SerializableFactory factory = factoryMap.get(type);

        if (factory != null) {
            return factory.make();
        }

        if (defaultFactory != null) {
            return defaultFactory.make(type);
        }

        throw new NoSuitableSerializableFactory(type);
    }
}
