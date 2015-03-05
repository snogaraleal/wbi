package client.ui.components;

import java.util.Map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import client.ui.components.utils.Script;

import client.ClientConf;

public class VectorMap extends Composite {
    public static enum Visual {
        EUROPE(
            "europe_mill_en",
            ClientConf.asset("jvectormap/jvectormap-europe-mill-en.js")),

        WORLD(
            "world_mill_en",
            ClientConf.asset("jvectormap/jvectormap-world-mill-en.js"));

        private String name;
        private String script;

        private Visual(String name, String script) {
            this.name = name;
            this.script = script;
        }

        public String getName() {
            return name;
        }

        public String getScript() {
            return script;
        }
    }

    public static enum Status {
        NEW,
        LOADING,
        READY
    }

    interface VectorMapUiBinder
        extends UiBinder<Widget, VectorMap> {}
    private static VectorMapUiBinder uiBinder =
        GWT.create(VectorMapUiBinder.class);

    @UiField
    DivElement div;

    private static final String BASE_SCRIPT =
        ClientConf.asset("jvectormap/jvectormap-2.0.1.min.js");

    private Visual visual;
    private Status status = Status.NEW;

    public VectorMap() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public VectorMap(Visual visual) {
        this();
        setVisual(visual);
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

    private void loadVisual(final Runnable callback) {
        switch (status) {
            case NEW:
                break;

            case LOADING:
                return;

            case READY:
                callback.run();
                return;
        }

        status = Status.LOADING;

        final Callback<Void, Exception> visualScriptCallback =
            new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception exception) {
                }

                @Override
                public void onSuccess(Void result) {
                    loadVisual(visual.getName());

                    status = Status.READY;
                    callback.run();
                }
            };

        final Callback<Void, Exception> baseScriptCallback =
            new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception exception) {
                }

                @Override
                public void onSuccess(Void result) {
                    Script.load(visual.getScript(), visualScriptCallback);
                }
            };

        Script.loadWithJQuery(BASE_SCRIPT, baseScriptCallback);
    }

    private native void loadVisual(String visualName) /*-{
        (function (that, $, jvm) {
            var div = $(that.@client.ui.components.VectorMap::div);

            that.map = new jvm.Map({
                container: div,
                map: visualName,
                backgroundColor: 'transparent',
                zoomOnScroll: false,
                regionStyle: {
                    initial: {
                        fill: '#DDDDDD'
                    },
                    hover: {
                        cursor: 'pointer',
                    }
                },
                series: {
                    regions: [{
                        values: {},
                        scale: ['#DDDDDD', '#666666'],
                        normalizeFunction: 'linear'
                    }],
                },
                onRegionTipShow: function(event, label, code) {
                    var data = that.series.values;

                    if (data[code] != undefined) {
                        label.html(
                            label.text() + ' (' +
                            Math.floor(data[code] * 10) / 10 + ')');
                    }
                },
            });

            that.series = that.map.series.regions[0];
        })(this, $wnd.jQuery, $wnd.jvm);
    }-*/;

    private JavaScriptObject mapToJSObject(Map<String, Double> data) {
        if (data == null) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            jsonObject.put(entry.getKey(), new JSONNumber(entry.getValue()));
        }

        return jsonObject.getJavaScriptObject();
    }

    public void setData(final Map<String, Double> data) {
        loadVisual(new Runnable() {
            @Override
            public void run() {
                setData(mapToJSObject(data));
            }
        });
    }

    private native void setData(JavaScriptObject data) /*-{
        (function (that, $, jvm) {
            var div = $(that.@client.ui.components.VectorMap::div);

            that.series.params.min = undefined;
            that.series.params.max = undefined;
            that.series.setValues(data);

            div.css('opacity', 0);

            setTimeout(function () {
                that.map.updateSize();
                div.css('opacity', 1);
            }, 0);
        })(this, $wnd.jQuery, $wnd.jvm);
    }-*/;
}
