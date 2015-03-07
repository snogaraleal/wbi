package client.ui.coordinator;

import client.managers.Manager;

public class SimpleCoordinator<T extends Manager> {
    private T manager;
    private Manager.View<T> currentView;

    public SimpleCoordinator(T manager) {
        this.manager = manager;
    }

    public T getManager() {
        return manager;
    }

    public void setView(Manager.View<T> view) {
        if (currentView != null) {
            currentView.onDetach(manager);
        }

        currentView = view;

        view.onAttach(manager);
    }

    public Manager.View<T> getCurrentView() {
        return currentView;
    }
}
