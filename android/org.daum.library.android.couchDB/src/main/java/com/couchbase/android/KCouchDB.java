package com.couchbase.android;

import android.content.*;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class KCouchDB implements ICouchDB {

    private Context ctx=null;

    /**
     * System shell
     */
    public static final String shell = "/system/bin/sh";


    /**
     * The URL Couchbase is running on
     */
    private static URL url;


    /**
     * Thread responsible for installing Couchbase
     */
    private CouchbaseInstaller couchbaseInstallThread;

    /**
     * Thread responsible for communicating with Couchbase process
     */
    private static Thread couchbaseRunThread;

    private KCouchDB self=null;
    private CouchbaseMobile couchbaseMobile;

    public KCouchDB(Context ctx){
        this.ctx = ctx;
        self= this;
        couchbaseMobile = new CouchbaseMobile(ctx);

    }


    public void completedInstall()
    {
        startCouchbase();
    }
    public void error()
    {
        System.err.println("ERROR");

    }
    public  void started()
    {

        System.err.println("STARTED");
    }

    public void stopCouchbase(){

    }




    /**
     * Install Couchbase in a separate thread
     */
    public void installCouchbase() throws InterruptedException {
        File f = new File(CouchbaseMobile.dataPath());
        f.mkdirs();
        couchbaseInstallThread = new CouchbaseInstaller(this);
        couchbaseInstallThread.start();
    }

    /**
     * Start the Couchbase service in a separate thread
     */
    public void startCouchbase()
    {

        String path = CouchbaseMobile.dataPath();
        String apkPath =path+ "/assets/install/couchDB.apk";
        try {
            new File(path + "/apk.ez").delete();
            Runtime.getRuntime().exec(new String[] { "ln", "-s", apkPath, path + "/apk.ez" });
        } catch (IOException e) {
            Log.v(CouchbaseMobile.TAG, "Error symlinking apk.ez", e);
            error();
            return;
        }

        String[] plainArgs = {
                "beam", "-K", "true", "--",
                "-noinput",
                "-boot_var", "APK", path + "/apk.ez",
                "-kernel", "inetrc", "\""+ path + "/erlang/bin/erl_inetrc\"",
                "-native_lib_path", path + "/lib",
                "-sasl", "errlog_type", "all",
                "-boot", path + "/erlang/bin/start",
                "-root", path + "/apk.ez/assets",
                "-eval", "code:load_file(jninif), R = application:start(couch), io:format(\"~w~n\",[R]).",
                "-couch_ini",
                path + "/couchdb/etc/couchdb/default.ini",
                path + "/couchdb/etc/couchdb/local.ini",
                path + "/couchdb/etc/couchdb/android.default.ini"};

        ArrayList<String> args = new ArrayList<String>(Arrays.asList(plainArgs));
        for (String iniPath : CouchbaseMobile.getCustomIniFiles()) {
            args.add(iniPath);
        }

        args.add(path + "/couchdb/etc/couchdb/overrides.ini");
        final String[] argv = args.toArray(new String[args.size()]);
        final String sopath = path + "/lib/libbeam.so";
        final String bindir = path + "/erlang/bin";

        System.load(sopath);
        System.load(path+"lib/libcom_couchbase_android_ErlangThread.so");

        couchbaseRunThread = new Thread() {
            public void run() {
                ErlangThread.setHandler(self);
                ErlangThread.start_erlang(bindir, sopath, argv);
                Log.i(CouchbaseMobile.TAG, "Erlang thread ended.");
            }
        };
        couchbaseRunThread.start();
    }



}
