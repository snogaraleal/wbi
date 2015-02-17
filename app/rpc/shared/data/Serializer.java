package rpc.shared.data;

public interface Serializer {
    String serialize(Object object)
        throws SerializerException;
    Object deserialize(String payload, Type expected)
        throws SerializerException;
}
