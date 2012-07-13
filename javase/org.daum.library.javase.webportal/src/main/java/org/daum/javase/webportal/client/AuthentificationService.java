package org.daum.javase.webportal.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 7/13/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AuthentificationService extends RemoteService {
    public void initHibernate();

    public boolean authenticateAgent(String name, String password);
}
