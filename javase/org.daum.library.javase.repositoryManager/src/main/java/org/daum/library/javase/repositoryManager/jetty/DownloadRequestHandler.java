package org.daum.library.javase.repositoryManager.jetty;


import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.repository.Repository;
import org.daum.library.javase.repositoryManager.DownloadRepository;
import org.daum.library.javase.repositoryManager.VirtualRepository;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.IO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.connector.wagon.WagonProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DownloadRequestHandler extends AbstractHandler{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public List<DownloadRepository> repositories= null;
    private final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";


    protected WagonProvider wagonProvider = null;

    public  DownloadRequestHandler(List<DownloadRepository> repositories){
        this.repositories = repositories;
    }


    private InputStream getServerStream(URL url) throws IOException {
        URLConnection conn = getConnection(url);
        InputStream is = conn.getInputStream();
        return is;
    }


    private URLConnection getConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection(Proxy.NO_PROXY);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        return conn;
    }


    private void download(URL url)
    {
        InputStream is = null;
        try {
            is = getServerStream(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(is, baos);

            byte[] file =baos.toByteArray();

        } catch (IOException e)
        {
            logger.error("download ", e);
        }
    }


    protected Wagon connectToWagonRepository(Repository repository) {
        try {
            String wagonHint = repository.getProtocol().toLowerCase(Locale.ENGLISH);
            Wagon wagon = wagonProvider.lookup(wagonHint);
            wagon.connect(repository, getAuthenticationInfo(repository.getId()));
            return wagon;
        } catch (Exception e) {
            getLog().warn("Unable to connect to wagon repository ["+repository.getId()+":"+repository.getUrl()+"]: "+e.getMessage(), e);
            return null;
        }
    }
    protected File downloadSingleFile(VirtualRepository virtualRepository,String artifactPath) {
        for (Repository repository : virtualRepository.getIgnoredRepositories()) {
            Wagon wagon = connectToWagonRepository(repository);
            if(wagon!=null){
                try {
                    if (wagon.resourceExists(artifactPath)) {
                        getLog().info("Artifact ["+artifactPath+"] already existing in repository ["+repository.getId()+"]");
                        return null;
                    }
                } catch (Exception e) {
                    getLog().warn("Check ignored repo for ["+artifactPath+"] failed: "+e.getMessage());
                }
            }
        }

    protected File downloadResource(VirtualRepository virtualRepository, String artifactPath) throws IOException {
        if(artifactPath.length()>0){
            artifactPath=artifactPath.substring(1);
        }

            File requestedFile = new File(virtualRepository.getStorageBase(), artifactPath);
            if(requestedFile.isFile()){
                if(virtualRepository.getCacheSeconds()<=0 || (System.currentTimeMillis()-requestedFile.lastModified()-virtualRepository.getCacheSeconds()<=0)){
                    return requestedFile;
                }
            }
            requestedFile = downloadSingleFile(virtualRepository,artifactPath);
            return requestedFile;

    }



    public void handle(String target, Request baseRequest,   			HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        File resource =null;

        System.out.println(target);

        if(resource!=null){
            System.out.println("Responding with file: "+resource.getAbsolutePath());
            FileInputStream fileInputStream=new FileInputStream(resource);
            IO.copy(fileInputStream, response.getOutputStream());
            fileInputStream.close();
            if(resource.getName().endsWith(".tmp.index.html")){
                resource.delete();
            }
            //proxyMojo.getLog().info("200 "+target);
        }else{
            //proxyMojo.getLog().info("404 "+target);
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"No artifact found in proxy");
        }
        baseRequest.setHandled(true);
    }


}
