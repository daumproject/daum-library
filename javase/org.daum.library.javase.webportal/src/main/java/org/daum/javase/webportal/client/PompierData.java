package org.daum.javase.webportal.client;


import com.google.gwt.core.client.GWT;


public class PompierData {

    private final AuthentificationServiceAsync loginService = GWT.create(AuthentificationService.class);
    private PompierRecord[] pompierRecords;

    public PompierData(){
        pompierRecords = getNewRecords();
    }

    public PompierRecord[] getNewRecords() {
        return new PompierRecord[]{
                new PompierRecord("Despagne", "Pierre"),
                new PompierRecord("Boschat", "Thierry"),
        };
    }
}