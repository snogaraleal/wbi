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
import javax.persistence.ManyToOne;

import rpc.shared.data.Type;

@SuppressWarnings("serial")
@Entity
public class Country extends Model {
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

    /*
     * {@code Serializable} implementation
     */

    public static final String FIELD_ISO = "iso";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_REGION = "region";

    @Override
    public Object get(String field) {
        if (field.equals(FIELD_ISO)) return iso;
        if (field.equals(FIELD_NAME)) return name;
        if (field.equals(FIELD_REGION)) return region;
        return super.get(field);
    }

    @Override
    public void set(String field, Object value) {
        super.set(field, value);
        if (field.equals(FIELD_ISO)) iso = (String) value;
        if (field.equals(FIELD_NAME)) name = (String) value;
        if (field.equals(FIELD_REGION)) region = (Region) value;
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
