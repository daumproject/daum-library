package org.daum.library.javase.apacheCouchDB;



import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 23/11/12
 * Time: 15:31
 */
@Library(name = "JavaSE")
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8888", optional = false)
})
@Provides({
        @ProvidedPort(name = "cluster", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE)
})
@ComponentType
public class ApacheCouchDB extends AbstractComponentType
{
    private  String basedir;
    private String jarfile;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public  void display(final Process p){
        Thread t =new Thread() {
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = "";
                    try {
                        while((line = reader.readLine()) != null) {
                            logger.debug(line);
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


    @Port(name = "cluster")
    public void fake( Object e){

    }
    @Start
    public void start()
    {
        JarFile apk = null;
        try
        {

             basedir =  System.getProperty("java.io.tmpdir")+File.separator+ "apachecouchdb";
             jarfile =  basedir+File.separator+ "apache-couchdb-binary.jar";

            File basefile = new File(basedir);
            FileManager.deleteOldFile(basefile);
            basefile.mkdirs();
             // copy jar
            FileOutputStream fosjar = new FileOutputStream(jarfile);

            //TODO system choose
            FileManager.copy(getClass().getClassLoader().getResourceAsStream("ubuntu32/apache-couchdb-binary.jar"), fosjar);

           //open jar
            apk = new JarFile(jarfile);
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

            final Process process = Runtime.getRuntime().exec(basedir+File.separator+"start.sh "+basedir);

            display(process);



        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


    @Stop
    public void stop()
    {
        try {
            Runtime.getRuntime().exec(basedir+File.separator+"stop.sh");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Update
    public void update()
    {

    }


}
