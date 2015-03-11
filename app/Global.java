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

import play.Application;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;

import data.tasks.CountryPopulateTask;
import data.tasks.IndicatorPopulateTask;
import data.tasks.TaskUtils;

import controllers.ServerConf;

/**
 * Play application {@code GlobalSettings} object.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        super.onStart(app);

        // Configure RPC mechanism
        ServerConf.configureRPC();

        // Run tasks for populating the database when the application starts
        TaskUtils.runTask(new CountryPopulateTask());
        TaskUtils.runTask(new IndicatorPopulateTask());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[] {
            GzipFilter.class
        };
    }
}
