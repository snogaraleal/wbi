package rpc.shared.data;

@SuppressWarnings("serial")
public class SerializerException extends Exception {
    public static enum Error {
        NOT_SERIALIZABLE,
        NOT_DESERIALIZABLE
    }

    public SerializerException(Error error) {
        super(getMessage(error));
    }

    public SerializerException(Error error, Throwable caught) {
        super(getMessage(error) + " (" + caught.toString() + ")");
    }

    public static String getMessage(Error error) {
        switch (error) {
            case NOT_SERIALIZABLE:
                return "Object not serializable";
            case NOT_DESERIALIZABLE:
                return "Object not deserializable";
            default:
                return "Serializer exception";
        }
    }
}
