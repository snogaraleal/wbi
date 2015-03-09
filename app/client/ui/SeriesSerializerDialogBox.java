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
