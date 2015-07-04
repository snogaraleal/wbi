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

/**
 * Registry of factories providing {@link Serializable} object instances.
 */
public class SerializableFactoryProvider {
    /**
     * Registered {@code SerializableFactory} objects.
     */
    private Map<Type, SerializableFactory> factoryMap =
        new HashMap<Type, SerializableFactory>();

    /**
     * {@code DefaultSerializableFactory} used when no
     * {@code SerializableFactory} is found.
     */
    private DefaultSerializableFactory defaultFactory;

    /**
     * Initialize {@code SerializableFactoryProvider}.
     */
    public SerializableFactoryProvider() {}

    /**
     * Initialize {@code SerializableFactoryProvider}.
     *
     * @param defaultFactory Default factory.
     */
    public SerializableFactoryProvider(
            DefaultSerializableFactory defaultFactory) {
        this();
        setDefaultFactory(defaultFactory);
    }

    /**
     * Register the specified {@link SerializableFactory}.
     *
     * @param type Type of object provided by the factory.
     * @param factory Factory to register.
     */
    public void addFactory(Type type, SerializableFactory factory) {
        factoryMap.put(type, factory);
    }

    /**
     * Replace the current {@link DefaultSerializableFactory}.
     *
     * @param defaultFactory Default factory.
     */
    public void setDefaultFactory(DefaultSerializableFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    /**
     * Make a new {@link Serializable} object of the specified type.
     *
     * @param type Type of object.
     * @return New instance.
     * @throws NoSuitableSerializableFactory
     */
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