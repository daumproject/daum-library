package org.kevoree.library.javase.webserver.collaborationToolsBasics.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.client.StructureService;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.AbstractItem;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.FileItem;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.FolderItem;

import java.io.File;
import java.util.List;


public class StructureServiceImpl extends RemoteServiceServlet implements StructureService{

    public AbstractItem getArborescence(AbstractItem folder) {
        String nameDirectory = folder.getName();
        File file = new File(nameDirectory);
        FolderItem root = new FolderItem(folder.getName());
        Process(file,root);
        trierListe(root.getChilds());
        return root;
    }

    public void Process(File file, FolderItem item) {
        if(!file.getName().contains(".git") && !file.getName().endsWith("~"))
        {
            if(file.isFile()){
                FileItem itemToAdd = new FileItem(file.getName());
                itemToAdd.setParent(item);
                itemToAdd.setPath(getItemPath(itemToAdd));
                item.add(itemToAdd);
            }
            else if (file.isDirectory()) {
                FolderItem folder = new FolderItem(file.getName());
                folder.setParent(item);
                folder.setPath(getItemPath(folder));
                item.add(folder);
                File[] listOfFiles = file.listFiles();
                if(listOfFiles!=null) {
                    for (int i = 0; i < listOfFiles.length; i++)
                        Process(listOfFiles[i],folder);
                }
            }
        }
    }

    private String getItemPath(AbstractItem item) {
        String pathItem = item.getName();
        String path = "";
        while(item.getParent() != null){
            path = item.getParent().getName()+"/" + path;
            item = item.getParent();
        }
        path = path + pathItem;
        return path;
    }



    private void trierListe(List<AbstractItem> listeTest) {
        int indexCurrentChar = 0;
        for(int i = 0 ; i < listeTest.size(); i++){
            if(listeTest.get(indexCurrentChar).getClass() == FileItem.class){
                listeTest.add(listeTest.get(indexCurrentChar));
                listeTest.remove(indexCurrentChar);
            }else{
                trierListe(((FolderItem) listeTest.get(indexCurrentChar)).getChilds());
                indexCurrentChar++;
            }
        }
    }
}