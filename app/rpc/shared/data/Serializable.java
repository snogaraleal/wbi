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

package rpc.shared.data;

import java.util.Map;

import rpc.shared.call.CallRequest;
import rpc.shared.call.CallResponse;

/**
 * Object that can be serialized.
 *
 * Only {@code Serializable} objects can be sent as arguments for a
 * {@link CallRequest} and as a result of a {@link CallResponse}.
 */
public interface Serializable {
    /**
     * Get the value of the specified field.
     *
     * @param field Field name.
     * @return Field value.
     */
    public Object get(String field);

    /**
     * Set the value of the specified field.
     *
     * @param field Field name.
     * @param value Field value.
     */
    public void set(String field, Object value);

    /**
     * Get specification of fields.
     *
     * @return Specification of fields.
     */
    public Map<String, Type> fields();
}
