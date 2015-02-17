package client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import client.dashboard.Dashboard;

public class Client implements EntryPoint {
    public void onModuleLoad() {
        ClientConf.configureRPC();

        Dashboard dashboard = new Dashboard();
        RootPanel.get().add(dashboard);
    }
}
