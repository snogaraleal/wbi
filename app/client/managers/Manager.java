package client.managers;

import com.google.gwt.user.client.ui.Widget;

public interface Manager {
    public static interface View<T> {
        void onAttach(T manager);
        void onDetach();

        Widget getWidget();
        T getCurrentManager();
    }
}
