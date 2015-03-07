package client.ui.coordinator;

import java.util.HashSet;
import java.util.Set;

import client.managers.Manager;

public class MultiCoordinator<T extends Manager> {
    private T manager;
    private Set<Manager.View<T>> views = new HashSet<Manager.View<T>>();

    public MultiCoordinator(T manager) {
        this.manager = manager;
    }

    public T getManager() {
        return manager;
    }

    public void addView(Manager.View<T> view) {
        if (!views.contains(view)) {
            views.add(view);
            view.onAttach(manager);
        }
    }

    public void removeView(Manager.View<T> view) {
        if (views.contains(view)) {
            views.remove(view);
            view.onDetach(manager);
        }
    }
}
