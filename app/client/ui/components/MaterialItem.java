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

/**
 * List item with enhanced look and feel.
 */
public class MaterialItem extends Composite
    implements HasClickHandlers, ClickHandler {

    /**
     * Icon displayed by a {@link MaterialItem}.
     */
    public static enum Icon {
        /**
         * Download icon.
         */
        DOWNLOAD(ClientConf.asset("icon/ic_download.svg")),

        /**
         * Delete icon.
         */
        DELETE(ClientConf.asset("icon/ic_delete.svg"));

        /**
         * Icon URL.
         */
        private String url;

        /**
         * Initialize icon.
         *
         * @param url Icon URL.
         */
        private Icon(String url) {
            this.url = url;
        }

        /**
         * Get icon URL.
         *
         * @return Icon URL.
         */
        public String getUrl() {
            return url;
        }
    }

    /**
     * {@link MaterialItem} element class.
     */
    public static enum Class {
        /**
         * Class added to animate the {@code MaterialItem}.
         */
        ANIMATE("animate"),

        /**
         * Class added to highlight the {@code MaterialItem}.
         */
        ACTIVE("active"),

        /**
         * Class added to a {@code MaterialItem} to show progress.
         */
        LOADING("loading");

        /**
         * Class name.
         */
        private String name;

        /**
         * Initialize {@code Class}.
         *
         * @param name Class name.
         */
        private Class(String name) {
            this.name = name;
        }

        /**
         * Get class name.
         *
         * @return Class name.
         */
        public String getName() {
            return name;
        }
    }

    public interface MaterialItemUiBinder
        extends UiBinder<Widget, MaterialItem> {}
    private static MaterialItemUiBinder uiBinder =
        GWT.create(MaterialItemUiBinder.class);

    /**
     * Main {@code Widget}.
     */
    @UiField
    public FocusPanel panel;

    /**
     * Ink for ripple animation.
     */
    @UiField
    public FlowPanel ink;

    /**
     * Item title.
     */
    @UiField
    public Label title;

    /**
     * Item subtitle.
     */
    @UiField
    public Label subtitle;

    /**
     * Item icon.
     */
    @UiField
    public Image icon;

    /**
     * Whether animations are enabled by default.
     */
    private static boolean DEFAULT_ANIMATION_ENABLED = false;

    /**
     * Whether animations are enabled.
     */
    private boolean animationEnabled;

    /**
     * Initialize {@code MaterialItem}.
     */
    public MaterialItem() {
        initWidget(uiBinder.createAndBindUi(this));
        panel.addClickHandler(this);
    }

    /**
     * Initialize {@code MaterialItem}.
     *
     * @param titleText Item title.
     * @param subtitleText Item subtitle.
     * @param animationEnabled Whether animations are enabled.
     */
    public MaterialItem(
            String titleText, String subtitleText,
            boolean animationEnabled) {

        this();

        setTitleText(titleText);
        setSubtitleText(subtitleText);
        setAnimationEnabled(animationEnabled);
    }

    /**
     * Initialize {@code MaterialItem}.
     *
     * @param titleText Item title.
     * @param subtitleText Item subtitle.
     */
    public MaterialItem(String titleText, String subtitleText) {
        this(titleText, subtitleText, DEFAULT_ANIMATION_ENABLED);
    }

    /**
     * Set whether animations are enabled.
     *
     * @param animationEnabled Whether animations are enabled.
     */
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

    /**
     * Set item title.
     *
     * @param text Item title.
     */
    public void setTitleText(String text) {
        title.setText(text);
    }

    /**
     * Set item subtitle.
     *
     * @param text Item subtitle.
     */
    public void setSubtitleText(String text) {
        subtitle.setText(text);
    }

    /**
     * Set item {@link Icon}.
     *
     * @param icon Item {@code Icon}.
     */
    public void setIcon(Icon icon) {
        this.icon.setUrl(icon.getUrl());
    }

    /**
     * Set whether the icon is visible.
     *
     * @param iconVisible Whether the icon is visible.
     */
    public void setIconVisible(boolean iconVisible) {
        icon.setVisible(iconVisible);
    }

    /**
     * Toggle the specified class.
     *
     * @param cls {@link Class} to toggle.
     * @param toggle Whether to add or remove the class.
     */
    private void toggleClass(Class cls, boolean toggle) {
        Element panelElement = panel.getElement();
        if (toggle) {
            panelElement.addClassName(cls.getName());
        } else {
            panelElement.removeClassName(cls.getName());
        }
    }

    /**
     * Set whether the item is active.
     *
     * @param active Whether the item is active.
     */
    public void setActive(boolean active) {
        toggleClass(Class.ACTIVE, active);
    }

    /**
     * Set whether the item is loading.
     *
     * @param loading Whether the item is loading.
     */
    public void setLoading(boolean loading) {
        toggleClass(Class.LOADING, loading);
    }
}
