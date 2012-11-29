package com.couchbase.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import android.util.Log;
import org.kevoree.kcl.KevoreeJarClassLoader;

/**
 * A thread which can install the requested version of Couchbase
 *
 */
public class CouchbaseInstaller extends Thread {

    /**
     * Prefix of files in the APK needing to be installed
     */
    private static final String INSTALL_ASSET_PREFIX = "assets/install/";

    /**
     * Files needing to have variables replaced
     */
    private static final String[] FILES_NEEDING_REPLACEMENTS = {
            "couchdb/bin/couchjs_wrapper",
            "couchdb/etc/couchdb/android.default.ini"
    };

    /**
     * Variables to be replaced
     */
    private static String[][] replacements = new String[][] {
            { "%couch_data_dir%", CouchbaseMobile.externalPath() },
            { "%couch_installation_dir%", CouchbaseMobile.dataPath() }
    };

    /**
     * The handler for communicating with the service thread
     */
    private KCouchDB handler;

    /**
     * Has this installation been cancelled
     */
    private boolean cancelled = false;

    /**
     * Create a new Thread to install Couchbase
     *
     * @param handler the handler for communicating with the service
     */
    public CouchbaseInstaller(KCouchDB handler) {
        this.handler = handler;
    }

    /**
     * Cancel this installation
     */
    public void cancelInstallation() {
        cancelled = true;
    }

    @Override
    public void run() {
        try {
            doInstall();
        } catch (IOException e) {
            Log.e(CouchbaseMobile.TAG, "Error installing Couchbase", e);
            handler.error();
        }
    }

    /**
     * Utility function to return the path to a file where we record the files installed
     *
     * @return the path to the index file
     */
    public static String indexFile() {
        return CouchbaseMobile.dataPath() + "/installedfiles.ser";
    }

    /**
     * Get a HashMap containing the CRCs of the files currently installed
     *
     * If no file can be found with the serialized HashMap, return a new empty one
     *
     * @return HashMap with String key (filename) and Long value (CRC)
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Long> getInstalledFilesCRC() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(indexFile());
            ois = new ObjectInputStream(fis);
            HashMap<String, Long> fileCRCs = (HashMap<String,Long>)ois.readObject();
            return fileCRCs;
        } catch (Exception e) {
            return new HashMap<String,Long>();
        }
        finally {
            try {
                if(ois != null) {
                    ois.close();
                }
                if(fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Log.v(CouchbaseMobile.TAG, "Exception closing installed files CRC streams");
            }
        }
    }

    /**
     * Update the HashMap containing the CRCs of the files installed
     *
     * @param fileCRCs the updated HashMap
     * @throws java.io.IOException
     */
    public void setInstalledFilesCRC(HashMap<String, Long> fileCRCs) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File indexFile = new File(indexFile());
            createFileAndParentDirectoriesIfNecessary(indexFile);
            fos = new FileOutputStream(indexFile());
            oos = new ObjectOutputStream(fos);
            oos.writeObject(fileCRCs);
        }
        finally {
            try {
                oos.close();
                fos.close();
            } catch (IOException e) {
                Log.e(CouchbaseMobile.TAG, "Exception closing installed files CRC streams");
            }
        }
    }

    /**
     * Perform the installation
     *
     * @throws java.io.IOException
     */
    public void doInstall() throws IOException
    {
        HashMap<String, Long> installedFilesCRCs = getInstalledFilesCRC();
        URL path_jar=null;
        if( getClass().getClassLoader() instanceof  KevoreeJarClassLoader)
        {
            KevoreeJarClassLoader  classLoader = (KevoreeJarClassLoader) getClass().getClassLoader();
            path_jar = classLoader.getLinkedLoadedURLs().get(0);
        }
        File path = new File(CouchbaseMobile.dataPath());
        if(!path.mkdirs()){
            Log.e(CouchbaseMobile.TAG, "Create PATH  "+CouchbaseMobile.dataPath());
        }


        JarFile apk = new JarFile(path_jar.getPath());
        int numberOfFilesUpdated = 0;
        Enumeration<JarEntry> e = apk.entries();
        while(e.hasMoreElements() && !cancelled) {
            JarEntry entry = e.nextElement();
            String name = entry.getName();

            if(name.startsWith(INSTALL_ASSET_PREFIX) && !entry.isDirectory()) {

                String restOfName = name.substring(INSTALL_ASSET_PREFIX.length());
                String fullName = CouchbaseMobile.dataPath() + restOfName;

                Log.v(CouchbaseMobile.TAG, "Checking CRC of " + fullName);

                Long crc = entry.getCrc();
                Long installedCRC = installedFilesCRCs.get(fullName);
                if(!crc.equals(installedCRC)) {
                    if(installedCRC != null) {
                        Log.v(CouchbaseMobile.TAG, "Need to update.  Installed: " + Long.toHexString(installedCRC) + " APK: " + Long.toHexString(crc));
                    }
                    else {
                        Log.v(CouchbaseMobile.TAG, "Need to update, new file with CRC: " + Long.toHexString(crc));
                    }
                    //need to update the file
                    File file = new File(fullName);
                    createFileAndParentDirectoriesIfNecessary(file);

                    FileOutputStream fos = new FileOutputStream(fullName);
                    copy(apk.getInputStream(entry), fos);

                    //if this file needs replacement, do it now
                    if(Arrays.asList(FILES_NEEDING_REPLACEMENTS).contains(restOfName)) {
                        Log.v(CouchbaseMobile.TAG, "Performing replacements in " + restOfName);
                        replace(fullName, replacements);
                    }

                    //now update the hash
                    installedFilesCRCs.put(fullName, crc);
                    numberOfFilesUpdated++;
                }
                else {
                    Log.v(CouchbaseMobile.TAG, "CRCs match.  Installed: " + Long.toHexString(installedCRC) + " APK: " + Long.toHexString(crc));
                }
            }

        }

        if(!cancelled) {
            //now write our installed CRCs back to disk
            setInstalledFilesCRC(installedFilesCRCs);

            Log.v(CouchbaseMobile.TAG, "Updated " + numberOfFilesUpdated + " files");

            handler.completedInstall();
        }
    }





    /**
     * Utility to copy bytes from an InputStream to an OutputStream
     * @param is the InputStream
     * @param os the OutputStream
     * @throws java.io.IOException
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {

        final int COPY_BUFFER = 2048;

        BufferedOutputStream bos = new BufferedOutputStream(os, COPY_BUFFER);
        BufferedInputStream bis = new BufferedInputStream(is);

        int count;
        byte data[] = new byte[COPY_BUFFER];

        while ((count = bis.read(data, 0, COPY_BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        bos.flush();
        bos.close();

        os.flush();
        os.close();
    }

    /**
     * Utility to create a file and all parent directories if necesary
     *
     * @param file the file to be created
     * @throws java.io.IOException
     */
    public static void createFileAndParentDirectoriesIfNecessary(File file) throws IOException {
        if(!file.exists()) {
            File parent = file.getParentFile();
            if(!parent.mkdirs())
            {
                System.err.println("createFileAndParentDirectoriesIfNecessary "+parent.getAbsolutePath());
            }

            parent.canExecute();
            parent.canRead();
            parent.canWrite();


            file.createNewFile();
            Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
        }
    }

    /**
     * Utilty to process a file line by line, replacing strings with values
     *
     * @param fileName the name of the file to process
     * @param replacements array of 2 element string arrays, 0 is string to match, 1 is replacement
     * @throws java.io.IOException
     */
    static void replace(String fileName, String[][] replacements)
            throws IOException {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "", content = "";
        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }

        for (int i = 0; i < replacements.length; i++) {
            content = content
                    .replaceAll(replacements[i][0], replacements[i][1]);
        }
        reader.close();
        FileWriter writer = new FileWriter(fileName);
        writer.write(content);
        writer.close();
        Runtime.getRuntime().exec("chmod 755 " + fileName);
    }

}
