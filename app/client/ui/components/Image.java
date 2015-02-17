package client.ui.components;

import client.ClientConf;

public class Image extends com.google.gwt.user.client.ui.Image {
    public void setAsset(String url) {
        setUrl(ClientConf.asset(url));
    }
}
