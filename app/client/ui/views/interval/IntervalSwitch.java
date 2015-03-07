package client.ui.views.interval;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import client.managers.IntervalManager;
import client.ui.components.MaterialButton;

public class IntervalSwitch extends Composite
    implements IntervalManager.View, IntervalManager.Listener {

    public static class Button extends MaterialButton {
        private IntervalSwitch intervalSwitch;
        private IntervalManager.Option option;

        public Button(
                IntervalSwitch intervalSwitch,
                IntervalManager.Option option) {

            super();

            this.intervalSwitch = intervalSwitch;

            setOption(option);
            setAnimationEnabled(true);
        }

        public void setOption(IntervalManager.Option option) {
            this.option = option;

            setText(option.toString());
        }

        @Override
        public void onClick(ClickEvent event) {
            super.onClick(event);
            IntervalManager manager = intervalSwitch.getCurrentManager();
            manager.select(option);
        }
    }

    interface IntervalSwitchUiBinder
        extends UiBinder<Widget, IntervalSwitch> {}
    private static IntervalSwitchUiBinder uiBinder =
        GWT.create(IntervalSwitchUiBinder.class);

    private Map<IntervalManager.Option, Button> map =
        new HashMap<IntervalManager.Option, Button>();

    private IntervalManager manager;

    @UiField
    FlowPanel panel;

    public IntervalSwitch() {
        initWidget(uiBinder.createAndBindUi(this));

        for (IntervalManager.Option option : IntervalManager.OPTIONS) {
            Button button = new Button(this, option);
            map.put(option, button);
            panel.add(button);
        }
    }

    @Override
    public void onSelect(IntervalManager.Option option) {
        for (Button button : map.values()) {
            button.setSelected(false);
        }

        Button button = map.get(option);
        if (button != null) {
            button.setSelected(true);
        }
    }

    @Override
    public void onAttach(IntervalManager manager) {
        assert this.manager == null;

        this.manager = manager;
        this.manager.addListener(this);

        onSelect(manager.getSelectedOption());
    }

    @Override
    public void onDetach() {
        assert this.manager != null;

        this.manager.removeListener(this);
        this.manager = null;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public IntervalManager getCurrentManager() {
        return manager;
    }
}
