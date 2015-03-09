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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class MaterialButton
    extends Composite implements HasText, HasClickHandlers, ClickHandler {

    public static enum Class {
        ANIMATE("animate"),
        SELECTED("selected");

        private String name;

        private Class(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    interface MaterialButtonUiBinder
        extends UiBinder<Widget, MaterialButton> {}
    private static MaterialButtonUiBinder uiBinder =
        GWT.create(MaterialButtonUiBinder.class);

    @UiField
    Anchor anchor;

    @UiField
    SpanElement label;

    @UiField
    SpanElement ink;

    private static final boolean DEFAULT_ANIMATION_ENABLED = false;
    private boolean animationEnabled;

    public MaterialButton() {
        initWidget(uiBinder.createAndBindUi(this));
        anchor.addClickHandler(this);
    }

    public MaterialButton(String text, boolean animationEnabled) {
        this();

        setText(text);
        setAnimationEnabled(animationEnabled);
    }

    public MaterialButton(String text) {
        this(text, DEFAULT_ANIMATION_ENABLED);
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (animationEnabled) {
            ink.removeClassName(Class.ANIMATE.getName());

            Style style = ink.getStyle();
            int size = anchor.getOffsetWidth();

            style.setWidth(size, Style.Unit.PX);
            style.setHeight(size, Style.Unit.PX);
            style.setMarginLeft(-size / 2, Style.Unit.PX);
            style.setMarginTop(-size / 2, Style.Unit.PX);

            style.setLeft(event.getX(), Style.Unit.PX);
            style.setTop(event.getY(), Style.Unit.PX);

            ink.addClassName(Class.ANIMATE.getName());
        }
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return anchor.addClickHandler(handler);
    }

    @Override
    public void setText(String text) {
        label.setInnerText(text);
    }

    @Override
    public String getText() {
        return label.getInnerText();
    }

    private void toggleClass(Class cls, boolean toggle) {
        Element anchorElement = anchor.getElement();
        if (toggle) {
            anchorElement.addClassName(cls.getName());
        } else {
            anchorElement.removeClassName(cls.getName());
        }
    }

    public void setSelected(boolean selected) {
        toggleClass(Class.SELECTED, selected);
    }
}
