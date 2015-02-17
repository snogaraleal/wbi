package models;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import rpc.shared.data.Type;

@Entity
public class Indicator extends Model {
    private static final long serialVersionUID = 146833399501199887L;

    public static Finder<Indicator> objects =
        new Finder<Indicator>(Indicator.class);

    public enum Status {
        AVAILABLE,
        LOADING,
        READY
    }

    protected String ident;
    protected String name;
    protected Status status;
    @ManyToMany protected List<Topic> topics;
    @ManyToOne protected Source source;

    public Indicator() {
        super();
    }

    public Indicator(
            String ident, String name, Status status,
            List<Topic> topics, Source source) {

        this();

        this.ident = ident;
        this.name = name;
        this.status = status;

        this.topics = topics;
        this.source = source;
    }

    public Indicator(
            String ident, String name,
            List<Topic> topics, Source source) {

        this(ident, name, Status.AVAILABLE, topics, source);
    }

    public String getIdent() {
        return ident;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isReady() {
        return status == Status.READY;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public Source getSource() {
        return source;
    }

    public static String FIELD_IDENT = "ident";
    public static String FIELD_NAME = "name";
    public static String FIELD_STATUS = "status";
    public static String FIELD_TOPICS = "topics";
    public static String FIELD_SOURCE = "source";

    public Object get(String field) {
        if (field == FIELD_IDENT) return ident;
        if (field == FIELD_NAME) return name;
        if (field == FIELD_STATUS) return status;
        if (field == FIELD_TOPICS) return topics;
        if (field == FIELD_SOURCE) return source;
        return super.get(field);
    }

    @SuppressWarnings("unchecked")
    public void set(String field, Object value) {
        super.set(field, value);
        if (field == FIELD_IDENT) ident = (String) value;
        if (field == FIELD_NAME) name = (String) value;
        if (field == FIELD_STATUS) status = (Status) value;
        if (field == FIELD_TOPICS) topics = (List<Topic>) value;
        if (field == FIELD_SOURCE) source = (Source) value;
    }

    private static Map<String, Type> fields;

    public Map<String, Type> fields() {
        if (fields == null) {
            fields = super.fields();
            fields.put(FIELD_IDENT, Type.get(String.class));
            fields.put(FIELD_NAME, Type.get(String.class));
            fields.put(FIELD_STATUS, Type.get(Status.class));
            fields.put(FIELD_TOPICS, Type.get(
                List.class, Type.get(Topic.class)));
            fields.put(FIELD_SOURCE, Type.get(Source.class));
        }
        return fields;
    }
}
