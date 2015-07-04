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

package rpc.shared.data.factory;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

/**
 * Factory providing instances of different types of
 * {@link Serializable} objects.
 */
public interface DefaultSerializableFactory {
    /**
     * Create a new {@link Serializable} object.
     *
     * @param type Type of object.
     * @return New instance of {@code Serializable} object.
     */
    public Serializable make(Type type);
}