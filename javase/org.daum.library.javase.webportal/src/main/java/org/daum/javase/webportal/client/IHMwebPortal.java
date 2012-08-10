package org.daum.javase.webportal.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IHMwebPortal implements EntryPoint {

    private final WebServiceAsync loginService = GWT
            .create(WebService.class);
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel.get().add(new AuthentiForm());
    }
}

