package org.kevoree.library.javase.webserver.collaborationToolsBasics.shared;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 6/14/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class FolderItem extends AbstractItem implements IsSerializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<AbstractItem> listeItem;

    public FolderItem(){}


    public FolderItem(String name){
        this.name = name;
        this.listeItem = new ArrayList<AbstractItem>();
    }

    public void add(AbstractItem itemToAdd){
        this.listeItem.add(itemToAdd);
    }

    public List<AbstractItem> getChilds(){
        return this.listeItem;
    }

}
