package org.daum.library.ormH.api;

import org.daum.library.ormH.utils.PersistenceException;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 18:52
 */
public interface IPersistenceSession {
    public void save(Object bean) throws PersistenceException;
    public void update(Object bean) throws PersistenceException;
    public void delete(Object bean) throws PersistenceException;
    public Object get(Class clazz,Object _id) throws PersistenceException;
    public Object getAll(Class clazz) throws PersistenceException;
}
