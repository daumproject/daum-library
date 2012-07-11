package org.daum.library.ormH.api;

import org.daum.library.ormH.utils.PersistenceException;

import java.util.Map;

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
    public <T> T get(Class<T> clazz,Object _id) throws PersistenceException;
    public <K extends Object, T> Map<K, T> getAll(Class<T> clazz) throws PersistenceException;
}
