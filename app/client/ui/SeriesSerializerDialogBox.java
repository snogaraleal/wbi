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

package client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import client.managers.models.SeriesManager;
import client.ui.components.MaterialButton;

public class SeriesSerializerDialogBox extends DialogBox {
    private static final int TEXT_AREA_WIDTH = 80;
    private static final int TEXT_AREA_HEIGHT = 24;

    private TextArea textArea = new TextArea();

    private MaterialButton selectAllButton =
        new MaterialButton("Select all", true);
    private MaterialButton closeButton =
        new MaterialButton("Close", true);

    private HorizontalPanel buttonsPanel = new HorizontalPanel();
    private VerticalPanel mainPanel = new VerticalPanel();

    public SeriesSerializerDialogBox() {
        super();

        setText("Serializer");
        setModal(true);
        setGlassEnabled(true);

        textArea.setCharacterWidth(TEXT_AREA_WIDTH);
        textArea.setVisibleLines(TEXT_AREA_HEIGHT);

        buttonsPanel.add(selectAllButton);
        buttonsPanel.add(closeButton);

        mainPanel.add(textArea);
        mainPanel.add(buttonsPanel);

        add(mainPanel);

        selectAllButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                textArea.selectAll();
                textArea.setFocus(true);
            }
        });

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
    }

    @Override
    public void center() {
        getElement().addClassName("fadein");
        super.center();
    }

    public void setContent(String content) {
        textArea.setText(content);
    }

    public static class OpenClickHandler implements ClickHandler {
        private SeriesManager manager;
        private SeriesManager.Serializer serializer;

        public OpenClickHandler(
                SeriesManager manager,
                SeriesManager.Serializer serializer) {

            this.manager = manager;
            this.serializer = serializer;
        }

        @Override
        public void onClick(ClickEvent event) {
            SeriesSerializerDialogBox dialogBox = new SeriesSerializerDialogBox();
            dialogBox.setContent(manager.serialize(serializer));
            dialogBox.center();
        }
    }
}
