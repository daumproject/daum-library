package eu.powet.fota;

import eu.powet.fota.events.FotaaEvent;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 17/07/12
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class Nativelib {


    public native int write_on_the_air_program(String port_device,int target,String path_hex_file);
    public native boolean register();
    public native void close_flash();



    public void callback(int val) {
        System.out.println("Native call "+val);
    }



    private static Nativelib singleton = null;

    public static Nativelib getInstance()
    {
        if(singleton == null){
            configure();
            singleton = new Nativelib();
        }
        return singleton;
    }

    private static void configure()
    {
        if (singleton == null)
        {
            try {
                File folder = new File(System.getProperty("java.io.tmpdir") + File.separator + "fotanative");
                if (folder.exists())
                {
                    deleteOldFile(folder);
                }
                folder.mkdirs();
              //  String absolutePath = copyFileFromStream(getPath("native.so"), folder.getAbsolutePath(), "fota" + getExtension());
               // load native
                System.load("/home/jed/githubJED/org.powet.fota/eu.powet.fota.native/nix32/target/eu.powet.fota.native.nix32.so");

            } catch (Exception e) {
                e.printStackTrace();
            }
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
        InputStream inputStream = Nativelib.class.getClassLoader().getResourceAsStream(inputFile);
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




}
