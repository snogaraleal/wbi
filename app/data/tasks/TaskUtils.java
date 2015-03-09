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

import java.util.concurrent.TimeUnit;

import play.libs.Akka;

import scala.concurrent.duration.Duration;

public class TaskUtils {
    public static void runTask(Runnable task) {
        Akka.system().scheduler().scheduleOnce(
            Duration.create(0, TimeUnit.MILLISECONDS),
            task, Akka.system().dispatcher());
    }
}
