package org.daum.library.javase.repositoryManager.jetty;


import org.daum.library.javase.repositoryManager.HttpProxyMojo;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.IO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadRequestHandler extends AbstractHandler{

	private HttpProxyMojo proxyMojo;

	public DownloadRequestHandler(HttpProxyMojo proxyMojo) {
		this.proxyMojo=proxyMojo;
	}

	public void handle(String target, Request baseRequest,   			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {



		File resource =proxyMojo.resolveArtifact(target);

		if(resource!=null){
            System.out.println("Responding with file: "+resource.getAbsolutePath());
			FileInputStream fileInputStream=new FileInputStream(resource);
			IO.copy(fileInputStream, response.getOutputStream());
			fileInputStream.close();
			if(resource.getName().endsWith(".tmp.index.html")){
				resource.delete();
			}
			proxyMojo.getLog().info("200 "+target);
		}else{
			proxyMojo.getLog().info("404 "+target);
			response.sendError(HttpServletResponse.SC_NOT_FOUND,"No artifact found in proxy");
		}
		baseRequest.setHandled(true);
	}


}
