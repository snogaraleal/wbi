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

package client.managers.history;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

import models.Country;
import models.Indicator;

/**
 * Information related to a {@code HistoryState}.
 */
public class HistoryStateData implements Serializable {
    /**
     * Selected {@code Indicator}.
     */
    private Indicator indicator;

    /**
     * List of selected {@code Country} objects.
     */
    private List<Country> countries;

    public HistoryStateData() {}

    /**
     * Initialize {@code HistoryStateData}.
     *
     * @param indicator Selected indicator.
     * @param countries Selected countries.
     */
    public HistoryStateData(Indicator indicator, List<Country> countries) {
        this();

        this.indicator = indicator;
        this.countries = countries;
    }

    /**
     * Get selected indicator.
     *
     * @return Selected indicator.
     */
    public Indicator getIndicator() {
        return indicator;
    }

    /**
     * Get selected countries.
     *
     * @return Selected countries.
     */
    public List<Country> getCountries() {
        return countries;
    }

    /*
     * {@code Serializable} implementation
     */

    public static final String FIELD_INDICATOR = "indicator";
    public static final String FIELD_COUNTRIES = "countries";

    @Override
    public Object get(String field) {
        if (field == FIELD_INDICATOR) return indicator;
        if (field == FIELD_COUNTRIES) return countries;
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(String field, Object value) {
        if (field == FIELD_INDICATOR) indicator = (Indicator) value;
        if (field == FIELD_COUNTRIES) {
            countries = (List<Country>) value;
        }
    }

    private static Map<String, Type> fields;

    @Override
    public Map<String, Type> fields() {
        if (fields == null) {
            fields = new HashMap<String, Type>();
            fields.put(FIELD_INDICATOR, Type.get(Indicator.class));
            fields.put(
                FIELD_COUNTRIES,
                Type.get(List.class, Type.get(Country.class)));
        }
        return fields;
    }
}
