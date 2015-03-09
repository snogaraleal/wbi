package client.ui.components;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import client.ClientConf;
import client.ui.components.utils.Script;

public class VectorMap extends Composite {
    public static enum Visual {
        EUROPE(
            "europe_mill_en",
            ClientConf.asset("js/jvectormap/jvectormap-europe-mill-en.js")),

        WORLD(
            "world_mill_en",
            ClientConf.asset("js/jvectormap/jvectormap-world-mill-en.js"));

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

    interface VectorMapUiBinder extends UiBinder<Widget, VectorMap> {}
    private static VectorMapUiBinder uiBinder =
        GWT.create(VectorMapUiBinder.class);

    @UiField
    DivElement div;

    public static final String BASE_SCRIPT =
        ClientConf.asset("js/jvectormap/jvectormap-2.0.1.min.js");
    public static final Script.Loader BASE_SCRIPT_LOADER =
        new Script.Loader(BASE_SCRIPT, Script.JQUERY);

    private Visual visual;
    private Script.Loader visualScriptLoader;
    private boolean visualLoaded = false;

    public VectorMap() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public VectorMap(Visual visual) {
        this();
        setVisual(visual);
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
        visualScriptLoader = new Script.Loader(
            visual.getScript(), BASE_SCRIPT_LOADER);
    }

    private void loadVisual(final Runnable callback) {
        visualScriptLoader.load(new Runnable() {
            @Override
            public void run() {
                if (!visualLoaded) {
                    loadVisual(visual.getName());
                    visualLoaded = true;
                }

                callback.run();
            }
        });
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
                        cursor: 'pointer'
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
                }
            });

            that.series = that.map.series.regions[0];
        })(this, $wnd.jQuery, $wnd.jvm);
    }-*/;

    private JavaScriptObject dataToJSObject(Map<String, Double> data) {
        if (data == null) {
            return null;
        }

        JSONObject dataObject = new JSONObject();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataObject.put(entry.getKey(), new JSONNumber(entry.getValue()));
        }

        return dataObject.getJavaScriptObject();
    }

    public void setData(final Map<String, Double> data) {
        loadVisual(new Runnable() {
            @Override
            public void run() {
                setData(dataToJSObject(data));
            }
        });
    }

    private native void setData(JavaScriptObject data) /*-{
        (function (that, $, jvm) {
            var div = $(that.@client.ui.components.VectorMap::div);

            that.map.reset();

            that.series.params.min = undefined;
            that.series.params.max = undefined;
            that.series.setValues(data);

            setTimeout(function () {
                that.map.updateSize();
            });
        })(this, $wnd.jQuery, $wnd.jvm);
    }-*/;
}
