package org.daum.library.android.touchDB;

import android.util.Log;
import org.kevoree.kcl.KevoreeJarClassLoader;
import java.io.*;
import java.net.URL;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 29/11/12
 * Time: 11:39
 */
public class NativeLibraryLoader implements INativeLoader {


    private static final String TAG = "NativeLibraryLoader";

    public  void deleteOldFile(File folder) {
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

    public String getExtension() {
        if (System.getProperty("os.name").toLowerCase().contains("nux")) {
            return ".so";
        }
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return ".dynlib";
        }
        return null;
    }

    public  String getPath(String lib) {
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

    public boolean is64() {
        String os = System.getProperty("os.arch").toLowerCase();
        return (os.contains("64"));
    }

    public String copyFileFromStream(String inputFile, String path, String targetName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFile);
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
    public boolean load() {
        try
        {
            Log.d(TAG, "Loading TDCollateJSON");
            String absolutePath= "";
            String libname = "libcom_couchbase_touchdb_TDCollateJSON.so";
            File folder = new File(System.getProperty("java.io.tmpdir") + File.separator + "CTouchDBLibraries");
            if (folder.exists())
            {
                deleteOldFile(folder);
            }
            folder.mkdirs();
            File filelib = new File(folder.getAbsolutePath()+File.separator+libname);
            // load the librairy with a different name - Bad approach :-) dalvik don't unload library in CL !!!
            String bad_approch = ""+new Random().nextInt(60000);
            absolutePath = copyFileFromStream("libs/armeabi/libcom_couchbase_touchdb_TDCollateJSON.so", folder.getAbsolutePath(),bad_approch+"libcom_couchbase_touchdb_TDCollateJSON.so");
            if(getClass().getClassLoader() instanceof KevoreeJarClassLoader)
            {
                KevoreeJarClassLoader  classLoader = (KevoreeJarClassLoader) getClass().getClassLoader();
                classLoader.addNativeMapping("TDCollateJSON",absolutePath);
                System.loadLibrary("TDCollateJSON");
            } else
            {
                System.err.println("Failback on System global load");
                System.load(absolutePath);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return  false;
        }
    }

    @Override
    public boolean unload() {
        //todo
        return false;
    }
}
