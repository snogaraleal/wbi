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

import java.util.Map;

import javax.persistence.Entity;

import rpc.shared.data.Type;

@SuppressWarnings("serial")
@Entity
public class Topic extends Model {
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

    /*
     * {@code Serializable} implementation
     */

    public static final String FIELD_IDENT = "ident";
    public static final String FIELD_NAME = "name";

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
