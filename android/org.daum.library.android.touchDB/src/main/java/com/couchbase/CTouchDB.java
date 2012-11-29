package com.couchbase;

import android.content.Context;
import org.kevoree.android.framework.helper.UIServiceHandler;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE", names = {"Android"})
@ComponentType
public class CTouchDB extends AbstractComponentType {

    private KCouchDB couchDB;

    @Start
    public void start()
    {
        Context ctx =     UIServiceHandler.getUIService().getRootActivity();
        couchDB = new KCouchDB(ctx);
        try
        {
            couchDB.installCouchbase();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Stop
    public void stop()
    {
        if(couchDB != null)
            couchDB.stopCouchbase();
    }


    @Update
    public void update()
    {


    }


}
