package org.daum.library.javase.apacheCouchDB;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/12/12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class FileManager {


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
    public static void createFileAndParentDirectoriesIfNecessary(File file)   {
        if(!file.exists()) {
            File parent = file.getParentFile();
            parent.mkdirs();



            parent.canExecute();
            parent.canRead();
            parent.canWrite();


            try {
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
}
