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

package client.ui.components;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import client.ClientConf;
import client.ui.components.utils.Script;

public class Chart extends Composite {
    public static class Series {
        private String label;
        private Map<Integer, Double> points;

        public Series(String label, Map<Integer, Double> points) {
            this.label = label;
            this.points = points;
        }

        public String getLabel() {
            return label;
        }

        public Map<Integer, Double> getPoints() {
            return points;
        }

        public static final String JS_OBJECT_POINTS = "data";
        public static final String JS_OBJECT_LABEL = "label";
        public static final String JS_OBJECT_SHADOW_SIZE = "shadowSize";

        public JSONObject toJSONObject() {
            String label = getLabel();
            Map<Integer, Double> points = getPoints();

            int pointsArrayIndex = 0;
            JSONArray pointsArray = new JSONArray();

            for (Map.Entry<Integer, Double> pointEntry : points.entrySet()) {
                JSONArray pointArray = new JSONArray();

                pointArray.set(0, new JSONNumber(pointEntry.getKey()));
                pointArray.set(1, new JSONNumber(pointEntry.getValue()));

                pointsArray.set(pointsArrayIndex, pointArray);
                pointsArrayIndex++;
            }

            JSONObject dataObject = new JSONObject();

            dataObject.put(JS_OBJECT_POINTS, pointsArray);
            dataObject.put(JS_OBJECT_LABEL, new JSONString(label));
            dataObject.put(JS_OBJECT_SHADOW_SIZE, new JSONNumber(0));

            return dataObject;
        }
    }

    public interface ChartUiBinder extends UiBinder<Widget, Chart> {}
    private static ChartUiBinder uiBinder =
        GWT.create(ChartUiBinder.class);

    @UiField
    public DivElement div;

    public static final String BASE_SCRIPT =
        ClientConf.asset("js/flot/jquery.flot.min.js");
    public static final Script.Loader BASE_SCRIPT_LOADER =
        new Script.Loader(BASE_SCRIPT, Script.JQUERY);

    public Chart() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void load(Runnable callback) {
        BASE_SCRIPT_LOADER.load(callback);
    }

    private JavaScriptObject seriesToJSObject(Series series) {
        return series.toJSONObject().getJavaScriptObject();
    }

    private JavaScriptObject seriesListToJSObject(
            Collection<Series> seriesCollection) {

        if (seriesCollection == null) {
            return null;
        }

        int dataArrayIndex = 0;
        JSONArray dataArray = new JSONArray();

        for (Series series : seriesCollection) {
            dataArray.set(dataArrayIndex, series.toJSONObject());
            dataArrayIndex++;
        }

        return dataArray.getJavaScriptObject();
    }

    public void setSeries(final Collection<Series> seriesCollection) {
        load(new Runnable() {
            @Override
            public void run() {
                setSeries(seriesListToJSObject(seriesCollection));
            }
        });
    }

    private native void setSeries(JavaScriptObject data) /*-{
        (function (that, $) {
            var div = $(that.@client.ui.components.Chart::div);

            setTimeout(function () {
                $.plot(div, data, {
                    series: {
                        lines: {
                            show: true,
                        },
                        points: {
                            show: true,
                        }
                    },
                    xaxis: {
                        tickSize: 1,
                        tickDecimals: 0
                    },
                    grid: {
                        margin: 20,
                        labelMargin: 20,
                        axisMargin: 20,
                        margin: 20,
                        borderColor: 'transparent',
                        hoverable: true
                    },
                    legend: {
                        noColumns: 6
                    }
                });
            });
        })(this, $wnd.jQuery);
    }-*/;
}
