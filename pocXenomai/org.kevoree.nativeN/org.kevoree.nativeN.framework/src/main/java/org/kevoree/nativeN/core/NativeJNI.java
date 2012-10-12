package org.kevoree.nativeN.core;

import java.io.*;
import java.util.EventObject;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 01/08/12
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class NativeJNI extends EventObject implements NativeEventPort {

    public native int init(int key,int port_event);
    public native boolean register();
    public native int start(int key,String path);
    public native int stop(int key);
    public native int update(int key);
    public native int create_input(int key,String name);
    public native int create_output(int key,String name);
    public native int enqueue(int key,int port,String msg);

    private NativeManager handler=null;

    public NativeJNI(NativeManager obj)
    {
        super(obj);
        this.handler =obj;
    }

    public void dispatchEvent(String queue,String evt)
    {
        handler.fireEvent(this,queue,evt);
    }


    public String configureCL()
    {
        try
        {
            File folder = new File(System.getProperty("java.io.tmpdir") + File.separator + "native");
            if (folder.exists())
            {
                deleteOldFile(folder);
            }
            folder.mkdirs();
             // todo
            String r = ""+new Random().nextInt(800);
            String absolutePath = copyFileFromStream(getPath("native.so"), folder.getAbsolutePath(),"libnative"+r+""+ getExtension());

            System.load(absolutePath);

            return absolutePath;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }



    /* Utility fonctions */
    public static void deleteOldFile(File folder) {
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    deleteOldFile(f);
                }
            }
        }
        folder.delete();
    }

    public static String getExtension() {
        if (System.getProperty("os.name").toLowerCase().contains("nux")) {
            return ".so";
        }
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return ".dynlib";
        }
        return null;
    }

    public static String getPath(String lib) {
        if (System.getProperty("os.name").toLowerCase().contains("nux")) {
            if (is64()) {
                return "nix64/"+lib;
            } else {
                return "nix32/"+lib;
            }
        }
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return "osx/"+lib;
        }
        return null;
    }

    public static boolean is64() {
        String os = System.getProperty("os.arch").toLowerCase();
        return (os.contains("64"));
    }

    public static String copyFileFromStream(String inputFile, String path, String targetName) throws IOException {
        InputStream inputStream = NativeJNI.class.getClassLoader().getResourceAsStream(inputFile);
        if (inputStream != null) {
            File copy = new File(path + File.separator + targetName);
            copy.deleteOnExit();
            OutputStream outputStream = new FileOutputStream(copy);
            byte[] bytes = new byte[1024];
            int length = inputStream.read(bytes);

            while (length > -1) {
                outputStream.write(bytes, 0, length);
                length = inputStream.read(bytes);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return copy.getAbsolutePath();
        }
        return null;
    }

    @Override
    public String getMessage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
