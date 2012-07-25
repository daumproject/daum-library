package org.daum.javase.webportal.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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

        //RootPanel.get().add(new AdministrationForm());
        loginService.loginFromSessionServer(new AsyncCallback<Agent>() {

            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get().add(new AuthentiForm());
            }

            @Override
            public void onSuccess(Agent result) {
                if (result == null) {
                    RootPanel.get().clear();
                    RootPanel.get().add(new AuthentiForm());
                } else if (result.isLogged()) {
                    RootPanel.get().clear();
                    RootPanel.get().add(new AdministrationForm());
                }
            }
        });

    }
}

