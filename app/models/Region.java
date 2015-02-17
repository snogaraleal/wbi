package models;

import java.util.Map;

import javax.persistence.Entity;

import rpc.shared.data.Type;

@Entity
public class Region extends Model {
    private static final long serialVersionUID = 4567433027504632946L;

    public static Finder<Region> objects =
        new Finder<Region>(Region.class);

    protected String ident;
    protected String name;

    public Region() {
        super();
    }

    public Region(String ident, String name) {
        this();

        this.ident = ident;
        this.name = name;
    }

    public String getIdent() {
        return ident;
    }

    public String getName() {
        return name;
    }

    public static String FIELD_IDENT = "ident";
    public static String FIELD_NAME = "name";

    public Object get(String field) {
        if (field == FIELD_IDENT) return ident;
        if (field == FIELD_NAME) return name;
        return super.get(field);
    }

    public void set(String field, Object value) {
        super.set(field, value);
        if (field == FIELD_IDENT) ident = (String) value;
        if (field == FIELD_NAME) name = (String) value;
    }

    private static Map<String, Type> fields;

    public Map<String, Type> fields() {
        if (fields == null) {
            fields = super.fields();
            fields.put(FIELD_IDENT, Type.get(String.class));
            fields.put(FIELD_NAME, Type.get(String.class));
        }
        return fields;
    }
}
