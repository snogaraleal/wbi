package models;

import java.util.Map;

import javax.persistence.Entity;

import rpc.shared.data.Type;

@Entity
public class Topic extends Model {
    private static final long serialVersionUID = -6908673654362184955L;

    public static Finder<Topic> objects =
        new Finder<Topic>(Topic.class);

    protected int ident;

    protected String name;

    public Topic() {
        super();
    }

    public Topic(int ident, String name) {
        this();

        this.ident = ident;
        this.name = name;
    }

    public int getIdent() {
        return ident;
    }

    public String getName() {
        return name;
    }

    public static String FIELD_IDENT = "ident";
    public static String FIELD_NAME = "name";

    @Override
    public Object get(String field) {
        if (field == FIELD_IDENT) return ident;
        if (field == FIELD_NAME) return name;
        return super.get(field);
    }

    @Override
    public void set(String field, Object value) {
        super.set(field, value);
        if (field == FIELD_IDENT) ident = (Integer) value;
        if (field == FIELD_NAME) name = (String) value;
    }

    private static Map<String, Type> fields;

    @Override
    public Map<String, Type> fields() {
        if (fields == null) {
            fields = super.fields();
            fields.put(FIELD_IDENT, Type.get(Integer.class));
            fields.put(FIELD_NAME, Type.get(String.class));
        }
        return fields;
    }
}
