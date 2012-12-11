package org.daum.library.javase.apacheCouchDB;




import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 06/12/12
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public class Compiler {


    public static void display(final Process p){
        Thread t =new Thread() {
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = "";
                    try {
                        while((line = reader.readLine()) != null) {
                            // Traitement du flux de sortie de l'application si besoin est
                            System.out.println(line);
                        }
                    } finally {
                        reader.close();
                    }
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };

        t.start();
    }

    public static void  main(String argv[]) throws IOException {


        try {
            JarFile apk = new JarFile("/home/jed/DAUM_PROJECT/daum-library/javase/org.daum.library.javase.apacheCouchDB/src/main/resources/apache-couchdb-install.jar");
            String basedir =   "/tmp/couchdb";
            String output = basedir+"/target";

            File target = new File(output);
            target.mkdirs();
            File basefile = new File(basedir);
            FileManager.deleteOldFile(basefile);


            int numberOfFilesUpdated = 0;
            Enumeration<JarEntry> e = apk.entries();
            while(e.hasMoreElements() ) {
                JarEntry entry = e.nextElement();
                String name = entry.getName();
                String fullName = basedir+"/"+name;
                File file = new File(fullName);
                FileManager.createFileAndParentDirectoriesIfNecessary(file);
                try{
                    FileOutputStream fos = new FileOutputStream(fullName);
                    FileManager.copy(apk.getInputStream(entry), fos);         }catch (FileNotFoundException te){

                }
            }

            final Process process = Runtime.getRuntime().exec(basedir+"/apache-couchdb-1.2.0/configure --prefix="+output);

            display(process);


            process.waitFor();



            final Process makeprocess = Runtime.getRuntime().exec("make");

            display(makeprocess);

            makeprocess.waitFor();


            final Process couchdb = Runtime.getRuntime().exec("couchdb");

            display(couchdb);

            couchdb.waitFor();
        } catch (InterruptedException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
