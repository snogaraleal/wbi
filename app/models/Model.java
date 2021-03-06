/*
 * WBI Indicator Explorer
 *
 * Copyright 2015 Sebastian Nogara <snogaraleal@gmail.com>
 *
 * This file is part of WBI.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

@SuppressWarnings("serial")
@MappedSuperclass
public class Model extends play.db.ebean.Model implements Serializable {
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

        public Finder(Class<T> type) {
            super(Long.class, type);
        }
    }

    /*
     * {@code Serializable} implementation
     */

    public static final String FIELD_ID = "id";

    @Override
    public Object get(String field) {
        if (field.equals(FIELD_ID)) return id;
        return null;
    }

    @Override
    public void set(String field, Object value) {
        if (field.equals(FIELD_ID)) id = (Long) value;
    }

    @Override
    public Map<String, Type> fields() {
        Map<String, Type> fields = new HashMap<String, Type>();
        fields.put(FIELD_ID, Type.get(Long.class));
        return fields;
    }
}
