package models;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;
import rpc.shared.data.factory.SerializableFactory;
import rpc.shared.data.factory.SerializableFactoryProvider;

public class ModelSerializableFactoryProvider
    extends SerializableFactoryProvider {

    private ModelSerializableFactoryProvider() {
        super();

        addFactory(Type.get(Topic.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Topic();
            }
        });

        addFactory(Type.get(Source.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Source();
            }
        });

        addFactory(Type.get(Region.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Region();
            }
        });

        addFactory(Type.get(Country.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Country();
            }
        });

        addFactory(Type.get(Indicator.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Indicator();
            }
        });

        addFactory(Type.get(Point.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Point();
            }
        });

        addFactory(Type.get(Series.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Series();
            }
        });
    }

    private static ModelSerializableFactoryProvider provider = null;

    public static ModelSerializableFactoryProvider get() {
        if (provider == null) {
            provider = new ModelSerializableFactoryProvider();
        }

        return provider;
    }
}
