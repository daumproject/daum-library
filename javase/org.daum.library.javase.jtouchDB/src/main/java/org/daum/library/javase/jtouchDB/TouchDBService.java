package org.daum.library.javase.jtouchDB;

import org.lightcouch.Response;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/02/13
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public interface TouchDBService {

    public Response save(String document,Object t);
    public Response update(String document,Object t);
    public Response remove(String document,Object t);

    public <T> T find(String document,Class<T> classType, String id);
    public <T> T findrev(String document,Class<T> classType, String id, String rev);
}
