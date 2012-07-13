package org.kevoree.library.javase.webserver.collaborationToolsBasics.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.AbstractItem;

import java.io.IOException;


@RemoteServiceRelativePath(value = "htmleditor")
public interface RepositoryToolsServices extends RemoteService {
    void createRepository(String login, String password, String nameRepository);

    void createFileToInitRepository(String url, String nomRepo);

    boolean updateContentFileAndCommit(byte [] editorText, String login);

    void cloneRepository(String url, String nameRepository);

    boolean commitRepository(String message, String nom, String email);

    boolean pushRepository(String login, String password);

    String getFileContent(String filePath) throws IOException;

    AbstractItem initRepository(String login, String password, String nameRepository);

    AbstractItem importRepository(String login, String password, String url);

    AbstractItem createFileIntoLocalRepository(AbstractItem item);

}
