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

package client.ui.components.utils;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.StyleInjector;

/**
 * Style utilities.
 */
public class Style {
    /**
     * Set of paths to loaded style files.
     */
    private static Set<String> loadedCss = new HashSet<String>();

    /**
     * Load the specified style file.
     *
     * @param css Path to style file.
     */
    public static void load(String css) {
        if (loadedCss.contains(css)) {
            return;
        }

        StyleInjector.inject(css, true);
        StyleInjector.flush();

        loadedCss.add(css);
    }
}
