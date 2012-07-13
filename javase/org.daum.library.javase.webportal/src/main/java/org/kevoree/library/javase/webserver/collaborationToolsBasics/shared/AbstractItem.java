package org.kevoree.library.javase.webserver.collaborationToolsBasics.shared;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 6/14/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.user.client.rpc.IsSerializable;



public abstract class AbstractItem implements IsSerializable

{
    protected String name;
    protected String path;
    protected AbstractItem parent = null;

    public AbstractItem(){}

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPath(){
        return this.path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public AbstractItem getParent() {
        return parent;
    }

    public void setParent(AbstractItem parent) {
        this.parent = parent;
    }
}
