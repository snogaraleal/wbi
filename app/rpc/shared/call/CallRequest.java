package rpc.shared.call;

import java.util.List;

import rpc.shared.UUID;

public class CallRequest {
    public static interface ServerSerializer {
        CallRequest deserialize(String payload) throws InvalidPayload;
    }

    public static interface ClientSerializer {
        String serialize(CallRequest request);
    }

    public static class Message {
        public static int SIZE = 4;

        public static int POSITION_CLASS_NAME = 0;
        public static int POSITION_METHOD_NAME = 1;
        public static int POSITION_TOKEN = 2;
        public static int POSITION_ARGUMENTS = 3;
    }

    private String className;
    private String methodName;
    private String token;
    private List<String> argumentPayloadList;

    public CallRequest(
            String className, String methodName,
            List<String> argumentPayloadList, String token) {

        this.className = className;
        this.methodName = methodName;
        this.argumentPayloadList = argumentPayloadList;
        this.token = token;
    }

    public CallRequest(
            String className, String methodName,
            List<String> argumentPayloadList) {

        this(className, methodName, argumentPayloadList, generateToken());
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    private static String generateToken() {
        return UUID.uuid();
    }

    public String getToken() {
        return token;
    }

    public List<String> getArgumentPayloadList() {
        return argumentPayloadList;
    }
}
