package client.ui.coordinator;

import client.managers.Manager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleCoordinator {
    private Manager manager;
    private Manager.View currentView;

    public SimpleCoordinator(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setView(Manager.View view) {
        if (currentView != null) {
            currentView.onDetach(manager);
        }

        currentView = view;

        view.onAttach(manager);
    }

    public Manager.View getCurrentView() {
        return currentView;
    }
}
