package client.ui.components;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import client.ClientConf;
import client.ui.components.utils.Script;

public class Chart extends Composite {
    interface ChartUiBinder
        extends UiBinder<Widget, Chart> {}
    private static ChartUiBinder uiBinder =
        GWT.create(ChartUiBinder.class);

    @UiField
    DivElement div;

    public static final String BASE_SCRIPT =
        ClientConf.asset("js/flot/jquery.flot.min.js");
    public static final Script.Manager baseScriptManager =
        new Script.Manager(BASE_SCRIPT, Script.JQUERY);

    public Chart() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void load(Runnable callback) {
        baseScriptManager.load(callback);
    }
}
