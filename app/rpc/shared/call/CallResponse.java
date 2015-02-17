package rpc.shared.call;

public class CallResponse {
    public static interface ServerSerializer {
        String serialize(CallResponse request);
    }

    public static interface ClientSerializer {
        CallResponse deserialize(String payload) throws InvalidPayload;
    }

    public static class Message {
        public static int SIZE = 3;

        public static int POSITION_TOKEN = 0;
        public static int POSITION_SUCCESS = 1;
        public static int POSITION_RETURN_VALUE = 2;
    }

    private String token;
    private boolean success;
    private String payload;

    public CallResponse(String token, boolean success, String payload) {
        this.token = token;
        this.success = success;
        this.payload = payload;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPayload() {
        return payload;
    }
}
