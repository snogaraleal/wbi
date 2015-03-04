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

    interface VectorMapUiBinder
        extends UiBinder<Widget, VectorMap> {}
    private static VectorMapUiBinder uiBinder =
        GWT.create(VectorMapUiBinder.class);

    @UiField
    DivElement div;

    private static final String BASE_SCRIPT =
        ClientConf.asset("jvectormap/jvectormap-2.0.1.min.js");

    public VectorMap() {
        initWidget(uiBinder.createAndBindUi(this));
    }

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

    public void create(final Visual visual, final Map<String, Double> data) {
        final Callback<Void, Exception> visualScriptCallback =
            new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception exception) {
                }

                @Override
                public void onSuccess(Void result) {
                    create(visual.getName(), mapToJSObject(data));
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

    private native void create(String visualName, JavaScriptObject data) /*-{
        if (this.map) {
            this.map.remove();
        }

        var div = $wnd.jQuery(this.@client.ui.components.VectorMap::div);

        div.vectorMap({
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
                values: data,
                scale: ['#DDDDDD', '#666666'],
                normalizeFunction: 'linear'
              }]
            },
            onRegionTipShow: function(event, label, code) {
              if (data[code] != undefined) {
                label.html(
                    label.text() + ' (' +
                    Math.floor(data[code] * 10) / 10 + ')');
              }
            },
        });

        var map = div.children('.jvectormap-container').data('mapObject');

        div.css('opacity', 0);
        setTimeout(function () {
            map.updateSize();
            div.css('opacity', 1);
        }.bind(this), 0);

        this.map = map;
    }-*/;
}
