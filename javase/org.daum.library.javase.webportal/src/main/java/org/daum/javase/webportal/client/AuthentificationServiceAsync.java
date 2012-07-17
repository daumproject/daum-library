package org.daum.javase.webportal.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AuthentificationServiceAsync {
      void initormh(AsyncCallback<Void> callback);
      void authenticateAgent(String name, String password, AsyncCallback<Boolean> callback);
}
