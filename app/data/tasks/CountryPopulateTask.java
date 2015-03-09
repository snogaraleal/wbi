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

package data.tasks;

import com.avaje.ebean.Ebean;

import models.Country;

import data.api.WorldBank;

public class CountryPopulateTask implements Runnable {
    @Override
    public void run() {
        if (Country.objects.findRowCount() > 0) {
            return;
        }

        WorldBank worldBank = new WorldBank().fetchCountryList();

        Ebean.save(worldBank.getRegionMap().values());
        Ebean.save(worldBank.getCountryMap().values());
    }
}
