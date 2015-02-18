package models;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import rpc.shared.data.Type;

@Entity
public class Country extends Model {
    private static final long serialVersionUID = -7919710522889048414L;

    public static Finder<Country> objects =
        new Finder<Country>(Country.class);

    protected String iso;

    protected String name;

    @ManyToOne
    protected Region region;

    public Country() {
        super();
    }

    public Country(String iso, String name, Region region) {
        this();

        this.iso = iso;
        this.name = name;
        this.region = region;
    }

    public String getISO() {
        return iso;
    }

    public String getName() {
        return name;
    }

    public Region getRegion() {
        return region;
    }

    public static String FIELD_ISO = "iso";
    public static String FIELD_NAME = "name";
    public static String FIELD_REGION = "region";

    @Override
    public Object get(String field) {
        if (field == FIELD_ISO) return iso;
        if (field == FIELD_NAME) return name;
        if (field == FIELD_REGION) return region;
        return super.get(field);
    }

    @Override
    public void set(String field, Object value) {
        super.set(field, value);
        if (field == FIELD_ISO) iso = (String) value;
        if (field == FIELD_NAME) name = (String) value;
        if (field == FIELD_REGION) region = (Region) value;
    }

    private static Map<String, Type> fields;

    @Override
    public Map<String, Type> fields() {
        if (fields == null) {
            fields = super.fields();
            fields.put(FIELD_ISO, Type.get(String.class));
            fields.put(FIELD_NAME, Type.get(String.class));
            fields.put(FIELD_REGION, Type.get(Region.class));
        }
        return fields;
    }
}
