package org.daum.javase.webportal.client;


import com.google.gwt.core.client.EntryPoint;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IHMwebPortal implements EntryPoint {


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        AuthentiForm authenificationForm = new AuthentiForm();
        authenificationForm.init();

    }
}

