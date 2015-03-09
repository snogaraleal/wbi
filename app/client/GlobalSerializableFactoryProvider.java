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

package client;

import rpc.shared.data.Serializable;
import rpc.shared.data.Type;
import rpc.shared.data.factory.SerializableFactory;
import rpc.shared.data.factory.SerializableFactoryProvider;

import models.Country;
import models.Indicator;
import models.Point;
import models.Region;
import models.Series;
import models.Source;
import models.Topic;

import client.managers.history.HistoryState;
import client.managers.history.HistoryStateData;

public class GlobalSerializableFactoryProvider
    extends SerializableFactoryProvider {

    private GlobalSerializableFactoryProvider() {
        super();

        addFactory(Type.get(Topic.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Topic();
            }
        });

        addFactory(Type.get(Source.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Source();
            }
        });

        addFactory(Type.get(Region.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Region();
            }
        });

        addFactory(Type.get(Country.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Country();
            }
        });

        addFactory(Type.get(Indicator.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Indicator();
            }
        });

        addFactory(Type.get(Point.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Point();
            }
        });

        addFactory(Type.get(Series.class), new SerializableFactory() {
            @Override
            public Serializable make() {
                return new Series();
            }
        });

        addFactory(
            Type.get(HistoryState.class),
            new SerializableFactory() {
                @Override
                public Serializable make() {
                    return new HistoryState();
                }
            });

        addFactory(
            Type.get(HistoryStateData.class),
            new SerializableFactory() {
                @Override
                public Serializable make() {
                    return new HistoryStateData();
                }
            });
    }

    private static GlobalSerializableFactoryProvider provider = null;

    public static GlobalSerializableFactoryProvider get() {
        if (provider == null) {
            provider = new GlobalSerializableFactoryProvider();
        }

        return provider;
    }
}
