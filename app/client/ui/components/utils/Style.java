package client.ui.components.utils;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.StyleInjector;

public class Style {
    private static Set<String> loadedCss = new HashSet<String>();

    public static void load(String css) {
        if (loadedCss.contains(css)) {
            return;
        }

        StyleInjector.inject(css, true);
        StyleInjector.flush();

        loadedCss.add(css);
    }
}
