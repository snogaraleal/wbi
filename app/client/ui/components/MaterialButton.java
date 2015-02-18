package client.ui.components;

import com.google.gwt.core.client.GWT;
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

    private static String CLASS_NAME_ANIMATE = "animate";
    private static String CLASS_NAME_SELECTED = "selected";

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

    private static boolean DEFAULT_ANIMATION_ENABLED = false;
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
            ink.removeClassName(CLASS_NAME_ANIMATE);

            Style style = ink.getStyle();
            int size = anchor.getOffsetWidth();

            style.setWidth(size, Style.Unit.PX);
            style.setHeight(size, Style.Unit.PX);
            style.setMarginLeft(-size / 2, Style.Unit.PX);
            style.setMarginTop(-size / 2, Style.Unit.PX);

            style.setLeft(event.getX(), Style.Unit.PX);
            style.setTop(event.getY(), Style.Unit.PX);

            ink.addClassName(CLASS_NAME_ANIMATE);
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

    public void setSelected(boolean selected) {
        if (selected) {
            anchor.getElement().addClassName(CLASS_NAME_SELECTED);
        } else {
            anchor.getElement().removeClassName(CLASS_NAME_SELECTED);
        }
    }
}
