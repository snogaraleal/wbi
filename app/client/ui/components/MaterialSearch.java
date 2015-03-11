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
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Search box.
 */
public class MaterialSearch extends Composite
    implements HasText, HasKeyUpHandlers {

    public interface MaterialSearchUiBinder
        extends UiBinder<Widget, MaterialSearch> {}
    private static MaterialSearchUiBinder uiBinder =
        GWT.create(MaterialSearchUiBinder.class);

    /**
     * Search icon.
     */
    @UiField
    public Image icon;

    /**
     * Text input.
     */
    @UiField
    public TextBox text;

    /**
     * Initialize {@code MaterialSearch}.
     */
    public MaterialSearch() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Set the placeholder of the text input.
     *
     * @param text Placeholder.
     */
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
