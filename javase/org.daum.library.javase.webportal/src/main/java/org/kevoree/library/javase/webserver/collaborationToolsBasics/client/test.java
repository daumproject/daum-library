package org.kevoree.library.javase.webserver.collaborationToolsBasics.client;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pdespagn
 * Date: 6/11/12
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class test {
    static int etage=-1;

    public static void main(String args[]){

       // System.out.print("1bonjour".substring(0,1));

        List<String> ffs = new ArrayList<String>();
        String nam = "/tmp/AccountTest";
        File aFile = new File(nam);
        Process(aFile,ffs);
        for(String vtff : ffs){
            System.out.println(vtff.toString());
        }


    }

    static void Process(File aFile, List<String> ffs) {
        etage++;
        if(!aFile.toString().contains(".git"))
        {
            if(aFile.isFile())
                ffs.add(etage + "[FILE]" + aFile.getName());
            else if (aFile.isDirectory()) {
                ffs.add(etage + "[DIR]" + aFile.getName());
                File[] listOfFiles = aFile.listFiles();
                if(listOfFiles!=null) {
                    for (int i = 0; i < listOfFiles.length; i++)
                        Process(listOfFiles[i],ffs);
                }
            }
        }
        etage--;
    }

    public static void listerRecursif(File file, String prefix, Set<String> liste) {
        if (file.exists()) {
            if (file.isFile()) {
                liste.add(prefix+file.getName());
            }
            else if (file.isDirectory()) {
                if(!file.getName().contains(".git"))
                {
                    File[] contenu	= file.listFiles();
                    liste.add(prefix+file.getName());
                    for(int i=0; i<contenu.length; i++) {
                        listerRecursif(contenu[i], prefix+file.getName()+"/", liste);
                    }
                }
            }
        }
    }

    // Deletes all files and subdirectories under dir
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    private static String getAccountName(String url) {
        String[] tableGit = url.split("/");
        String accountName = tableGit[3];
        return accountName;
    }

}
