package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

@MappedSuperclass
public class Model extends play.db.ebean.Model implements Serializable {
    private static final long serialVersionUID = -6487868681272642072L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    public Model() {
        super();
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (object == this) {
            return true;
        }

        if (!(object instanceof Model)) {
            return false;
        }

        if (this.getClass() != object.getClass()) {
            return false;
        }

        if (id == null) {
            return super.equals(object);
        }

        Model model = (Model) object;

        return this.getId().equals(model.getId());
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }

        int hash = 3;
        hash = 37 * hash + (int)(id ^ (id >>> 32));
        return hash;
    }

    public static class Finder<T>
        extends play.db.ebean.Model.Finder<Long, T> {

        private static final long serialVersionUID = 1L;

        public Finder(Class<T> type) {
            super(Long.class, type);
        }
    }

    public static String FIELD_ID = "id";

    @Override
    public Object get(String field) {
        if (field == FIELD_ID) return id;
        return null;
    }

    @Override
    public void set(String field, Object value) {
        if (field == FIELD_ID) id = (Long) value;
    }

    @Override
    public Map<String, Type> fields() {
        Map<String, Type> fields = new HashMap<String, Type>();
        fields.put(FIELD_ID, Type.get(Long.class));
        return fields;
    }
}
