package rpc.server.invoke;

public class InvokerException extends Exception {
    private static final long serialVersionUID = 7935064452648071758L;

    private static String GENERIC_MESSAGE = "Exception during invocation";

    public static enum Reason {
        INCOMPATIBLE_ARGUMENTS,
        INVALID_PAYLOAD,
        INVOKABLE_EXCEPTION,
        UNSUPPORTED_RETURN_VALUE
    }

    public InvokerException(Reason reason) {
        super(getMessage(reason));
    }

    public InvokerException(Reason reason, Throwable caught) {
        super(getMessage(reason) + " (" + caught + ")");
    }

    private static String getMessage(Reason reason) {
        switch (reason) {
            case INCOMPATIBLE_ARGUMENTS:
                return "Incompatible arguments";

            case INVALID_PAYLOAD:
                return "Invalid payload";

            case INVOKABLE_EXCEPTION:
                return "Exception in invokable";

            case UNSUPPORTED_RETURN_VALUE:
                return "Unsupported return value";

            default:
                return GENERIC_MESSAGE;
        }
    }
}
