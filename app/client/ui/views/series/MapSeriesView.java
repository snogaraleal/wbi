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

package client.ui.views.series;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import models.Country;
import models.Series;

import client.managers.models.SeriesManager;
import client.ui.components.VectorMap;

public class MapSeriesView extends SeriesView {
    interface MapSeriesViewUiBinder
        extends UiBinder<Widget, MapSeriesView> {}
    private static MapSeriesViewUiBinder uiBinder =
        GWT.create(MapSeriesViewUiBinder.class);

    @UiField
    VectorMap vectorMap;

    public MapSeriesView() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MapSeriesView(VectorMap.Visual visual) {
        this();
        vectorMap.setVisual(visual);
    }

    @Override
    public void onUpdate(
            List<SeriesManager.Row> rows,
            SortedSet<Integer> years,
            SeriesManager.Ordering ordering) {

        super.onUpdate(rows, years, ordering);

        Map<String, Double> data = new HashMap<String, Double>();

        for (SeriesManager.Row row : rows) {
            Series series = row.getSeries();
            Country country = series.getCountry();

            if (country != null) {
                data.put(country.getISO(), series.getAverage());
            }
        }

        vectorMap.setData(data);
    }

    @Override
    public void onChange(SeriesManager.Row row) {
        super.onChange(row);
    }
}
