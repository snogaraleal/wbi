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
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import client.ClientConf;

public class MaterialItem extends Composite
    implements HasClickHandlers, ClickHandler {

    public static enum Icon {
        DOWNLOAD(ClientConf.asset("icon/ic_download.svg")),
        DELETE(ClientConf.asset("icon/ic_delete.svg"));

        private String url;

        private Icon(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public static enum Class {
        ANIMATE("animate"),
        ACTIVE("active"),
        LOADING("loading");

        private String name;

        private Class(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public interface MaterialItemUiBinder
        extends UiBinder<Widget, MaterialItem> {}
    private static MaterialItemUiBinder uiBinder =
        GWT.create(MaterialItemUiBinder.class);

    @UiField
    public FlowPanel ink;

    @UiField
    public FocusPanel panel;

    @UiField
    public Label title;

    @UiField
    public Label subtitle;

    @UiField
    public Image icon;

    private static boolean DEFAULT_ANIMATION_ENABLED = false;
    private boolean animationEnabled;

    public MaterialItem() {
        initWidget(uiBinder.createAndBindUi(this));
        panel.addClickHandler(this);
    }

    public MaterialItem(
            String titleText, String subtitleText,
            boolean animationEnabled) {

        this();

        setTitleText(titleText);
        setSubtitleText(subtitleText);
        setAnimationEnabled(animationEnabled);
    }

    public MaterialItem(String titleText, String subtitleText) {
        this(titleText, subtitleText, DEFAULT_ANIMATION_ENABLED);
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (animationEnabled) {
            Element inkElement = ink.getElement();

            inkElement.removeClassName(Class.ANIMATE.getName());

            Style style = inkElement.getStyle();
            int size = panel.getOffsetWidth();

            style.setWidth(size, Style.Unit.PX);
            style.setHeight(size, Style.Unit.PX);
            style.setMarginLeft(-size / 2, Style.Unit.PX);
            style.setMarginTop(-size / 2, Style.Unit.PX);

            style.setLeft(event.getX(), Style.Unit.PX);
            style.setTop(event.getY(), Style.Unit.PX);

            inkElement.addClassName(Class.ANIMATE.getName());
        }
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return panel.addClickHandler(handler);
    }

    public void setTitleText(String text) {
        title.setText(text);
    }

    public void setSubtitleText(String text) {
        subtitle.setText(text);
    }

    public void setIcon(Icon icon) {
        this.icon.setUrl(icon.getUrl());
    }

    public void setIconVisible(boolean iconVisible) {
        icon.setVisible(iconVisible);
    }

    private void toggleClass(Class cls, boolean toggle) {
        Element panelElement = panel.getElement();
        if (toggle) {
            panelElement.addClassName(cls.getName());
        } else {
            panelElement.removeClassName(cls.getName());
        }
    }

    public void setActive(boolean active) {
        toggleClass(Class.ACTIVE, active);
    }

    public void setLoading(boolean loading) {
        toggleClass(Class.LOADING, loading);
    }
}
