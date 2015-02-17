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
    extends Composite implements HasText, HasClickHandlers {

    private static String CLASS_NAME_ANIMATE = "animate";

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

    public MaterialButton() {
        initWidget(uiBinder.createAndBindUi(this));

        anchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
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
        });
    }

    public MaterialButton(String text) {
        this();
        setText(text);
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
}
