package org.kevoree.library.javase.webserver.collaborationToolsBasics.shared;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 6/14/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.user.client.rpc.IsSerializable;

public class FileItem extends AbstractItem implements IsSerializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public FileItem(){}

    public FileItem(String name){
        this.name = name;
    }


}
