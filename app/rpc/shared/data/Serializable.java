package rpc.shared.data;

import java.util.Map;

public interface Serializable {
    public Object get(String field);
    public void set(String field, Object value);
    public Map<String, Type> fields();
}
