package org.daum.library.replica.disk;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/07/12
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class ADisk implements  IDiskPersistence{

    private  Boolean isenable=false;

    public Boolean getIsenable() {
        return isenable;
    }

    public void setIsenable(Boolean isenable) {
        this.isenable = isenable;
    }
}
