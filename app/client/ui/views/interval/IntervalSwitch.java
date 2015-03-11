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

import client.managers.models.IntervalManager;
import client.ui.components.MaterialButton;

/**
 * Interval selector implementing {@link IntervalManager.View}.
 */
public class IntervalSwitch extends Composite
    implements IntervalManager.View, IntervalManager.Listener {

    /**
     * {@link MaterialButton} displaying an {@link IntervalManager.Option}.
     */
    public static class Button extends MaterialButton {
        private IntervalSwitch intervalSwitch;
        private IntervalManager.Option option;

        /**
         * Initialize {@code Button}.
         *
         * @param intervalSwitch {@link IntervalSwitch} that created this button.
         * @param option {@link IntervalManager.Option} to display.
         */
        public Button(
                IntervalSwitch intervalSwitch,
                IntervalManager.Option option) {

            super();

            this.intervalSwitch = intervalSwitch;

            setOption(option);
            setAnimationEnabled(true);
        }

        /**
         * Set the {@link IntervalManager.Option} displayed in this button.
         *
         * @param option {@code IntervalManager.Option} to display.
         */
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

    public interface IntervalSwitchUiBinder
        extends UiBinder<Widget, IntervalSwitch> {}
    private static IntervalSwitchUiBinder uiBinder =
        GWT.create(IntervalSwitchUiBinder.class);

    /**
     * {@code Button} widgets by {@code IntervalManager.Option}.
     */
    private Map<IntervalManager.Option, Button> map =
        new HashMap<IntervalManager.Option, Button>();

    /**
     * {@code IntervalManager} this view is currently attached to.
     */
    private IntervalManager manager;

    /**
     * Panel containing {@link Button} widgets.
     */
    @UiField
    public FlowPanel panel;

    /**
     * Initialize {@code IntervalSwitch}.
     */
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
