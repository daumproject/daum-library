package org.kevoree.library.javase.webserver.collaborationToolsBasics.client;


import com.google.gwt.user.client.rpc.AsyncCallback;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.AbstractItem;


public interface RepositoryToolsServicesAsync {

    void createRepository(String login, String password, String nameRepository, AsyncCallback<Boolean> callback);

    void createFileToInitRepository(String url,  String nomRepo, AsyncCallback<Void> callback);

    void updateContentFileAndCommit(byte [] editorText,  String login, AsyncCallback<Boolean> callback);

    void cloneRepository(String url, String foldToPutRepo,AsyncCallback<Void> callback);

    void commitRepository(String message, String nom, String email, AsyncCallback<Boolean> callback);

    void pushRepository( String login, String password, AsyncCallback<Boolean> callback);

    void getFileContent(String filePath, AsyncCallback<String> callback);

    void  initRepository(String login, String password, String nameRepository,AsyncCallback<AbstractItem> callback) ;

    void  importRepository(String login, String password, String url,AsyncCallback<AbstractItem> callback);

    void createFileIntoLocalRepository(AbstractItem item, AsyncCallback<AbstractItem> callback);

}
