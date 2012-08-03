package org.daum.javase.webportal.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import org.daum.javase.webportal.shared.Agent;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IHMwebPortal implements EntryPoint {

    private final AuthentificationServiceAsync loginService = GWT
            .create(AuthentificationService.class);
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel.get().add(new AuthentiForm());
    }
}

