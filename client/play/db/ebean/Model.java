package play.db.ebean;

import javax.persistence.MappedSuperclass;

import java.util.Map;

@MappedSuperclass
public class Model {
    public static class Finder<I, T> {
        public Finder() {}
        public Finder(Class<I> idType, Class<T> type) {}
        public Finder(String serverName, Class<I> idType, Class<T> type) {}
    }

    public Model() {}
}
