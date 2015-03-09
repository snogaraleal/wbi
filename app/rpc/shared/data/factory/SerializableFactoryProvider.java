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

import java.util.HashMap;
import java.util.Map;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;

public class SerializableFactoryProvider {
    private DefaultSerializableFactory defaultFactory;
    private Map<Type, SerializableFactory> factoryMap =
        new HashMap<Type, SerializableFactory>();

    public SerializableFactoryProvider() {
    }

    public SerializableFactoryProvider(
            DefaultSerializableFactory defaultFactory) {
        this();
        setDefaultFactory(defaultFactory);
    }

    public void addFactory(Type type, SerializableFactory factory) {
        factoryMap.put(type, factory);
    }

    public void setDefaultFactory(DefaultSerializableFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    public Serializable make(Type type) throws NoSuitableSerializableFactory {
        SerializableFactory factory = factoryMap.get(type);

        if (factory != null) {
            return factory.make();
        }

        if (defaultFactory != null) {
            return defaultFactory.make(type);
        }

        throw new NoSuitableSerializableFactory(type);
    }
}
