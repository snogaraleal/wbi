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

    public static class Icon {
        public static String DOWNLOAD =
            ClientConf.asset("icon/ic_download.svg");
        public static String DELETE =
            ClientConf.asset("icon/ic_delete.svg");
    }

    public static class Class {
        public static String ANIMATE = "animate";
        public static String ACTIVE = "active";
        public static String LOADING = "loading";
    }

    interface MaterialItemUiBinder
        extends UiBinder<Widget, MaterialItem> {}
    private static MaterialItemUiBinder uiBinder =
        GWT.create(MaterialItemUiBinder.class);

    @UiField
    FlowPanel ink;

    @UiField
    FocusPanel panel;

    @UiField
    Label title;

    @UiField
    Label subtitle;

    @UiField
    Image icon;

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

            inkElement.removeClassName(Class.ANIMATE);

            Style style = inkElement.getStyle();
            int size = panel.getOffsetWidth();

            style.setWidth(size, Style.Unit.PX);
            style.setHeight(size, Style.Unit.PX);
            style.setMarginLeft(-size / 2, Style.Unit.PX);
            style.setMarginTop(-size / 2, Style.Unit.PX);

            style.setLeft(event.getX(), Style.Unit.PX);
            style.setTop(event.getY(), Style.Unit.PX);

            inkElement.addClassName(Class.ANIMATE);
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

    public void setIcon(String url) {
        icon.setUrl(url);
    }

    public void setIconVisible(boolean iconVisible) {
        icon.setVisible(iconVisible);
    }

    private void toggleClass(String className, boolean toggle) {
        Element panelElement = panel.getElement();
        if (toggle) {
            panelElement.addClassName(className);
        } else {
            panelElement.removeClassName(className);
        }
    }

    public void setActive(boolean active) {
        toggleClass(Class.ACTIVE, active);
    }

    public void setLoading(boolean loading) {
        toggleClass(Class.LOADING, loading);
    }
}
