package org.kevoree.nativeN.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 03/10/12
 * Time: 17:21
 * To change this template use File | Settings | File Templates.
 */
public class FileManager {


    public static byte[] load(InputStream reader) throws IOException {
        int c;
        ArrayList<Byte> tab = new ArrayList<Byte>();
        while((c = reader.read()) != -1) {
            tab.add((byte)c);
        }
        if (reader!=null)
            reader.close();
        return toByteArray(tab);
    }

    public static String copyFileFromStream( InputStream inputStream , String path, String targetName) throws IOException {

        if (inputStream != null) {
            File copy = new File(path + File.separator + targetName);
            //copy.deleteOnExit();
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
    public static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }

    public  static byte[] load(String pathfile)
    {
        File file = new File(pathfile);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        ArrayList<Byte> tab = new ArrayList<Byte>();
        try
        {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0)
            {
                tab.add((byte)dis.read());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
                bis.close();
                dis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return  toByteArray(tab);
    }

}
