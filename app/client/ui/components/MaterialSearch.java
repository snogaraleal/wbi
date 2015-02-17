package client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MaterialSearch
    extends Composite implements HasText, HasKeyUpHandlers {

    interface MaterialSearchUiBinder
        extends UiBinder<Widget, MaterialSearch> {}
    private static MaterialSearchUiBinder uiBinder =
        GWT.create(MaterialSearchUiBinder.class);

    @UiField
    Image icon;

    @UiField
    TextBox text;

    public MaterialSearch() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setPlaceholder(String text) {
        this.text.getElement().setPropertyString("placeholder", text);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return text.addKeyUpHandler(handler);
    }

    @Override
    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public String getText() {
        return text.getText();
    }
}
