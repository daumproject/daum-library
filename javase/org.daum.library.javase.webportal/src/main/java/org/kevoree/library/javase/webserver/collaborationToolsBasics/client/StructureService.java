package org.kevoree.library.javase.webserver.collaborationToolsBasics.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.kevoree.library.javase.webserver.collaborationToolsBasics.shared.AbstractItem;

@RemoteServiceRelativePath("systemFileServices")
public interface StructureService extends RemoteService {
	AbstractItem getArborescence(AbstractItem folder);

}
