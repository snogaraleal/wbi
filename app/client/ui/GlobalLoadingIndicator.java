package client.ui;

import com.google.gwt.user.client.ui.FlowPanel;

public class GlobalLoadingIndicator extends FlowPanel {
    public static final String CLASS_NAME_SPINNER = "spinner";

    private GlobalLoadingIndicator() {
        super();
        getElement().addClassName(CLASS_NAME_SPINNER);
        setVisible(false);
    }

    private static GlobalLoadingIndicator indicator;

    public static GlobalLoadingIndicator get() {
        if (indicator == null) {
            indicator = new GlobalLoadingIndicator();
        }

        return indicator;
    }

    private static int count = 0;

    private static void updateVisibility() {
        GlobalLoadingIndicator indicator = get();
        indicator.setVisible(count > 0);
    }

    public static void start() {
        count++;
        updateVisibility();
    }

    public static void finish() {
        count--;
        updateVisibility();
    }
}
