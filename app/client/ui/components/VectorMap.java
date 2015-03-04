package client.ui.components;

import java.util.Map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

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

    public static final String BASE_SCRIPT =
        ClientConf.asset("jvectormap/jvectormap-2.0.1.min.js");

    private Map<String, Double> data;

    public VectorMap() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setVisual(final Visual visual) {
        final Callback<Void, Exception> visualScriptCallback =
            new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception exception) {
                }

                @Override
                public void onSuccess(Void result) {
                    create();
                }
            };

        final Callback<Void, Exception> baseScriptCallback =
            new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception exception) {
                }

                @Override
                public void onSuccess(Void result) {
                    ScriptUtils.load(visual.getScript(), visualScriptCallback);
                }
            };

        ScriptUtils.loadWithJQuery(BASE_SCRIPT, baseScriptCallback);
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        create();
    }

    private void create() {
    }
}
