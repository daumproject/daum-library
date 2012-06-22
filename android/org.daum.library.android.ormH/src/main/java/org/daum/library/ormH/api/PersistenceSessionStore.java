package org.daum.library.ormH.api;

import org.daum.library.ormH.persistence.Orhm;
import org.daum.library.ormH.utils.PersistenceException;

import java.util.Map;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 14:33
 */
public interface PersistenceSessionStore
{
    public void save(Orhm id,Object bean) throws PersistenceException;
    public void update(Orhm id,Object bean)throws PersistenceException;
    public void delete(Orhm id) throws PersistenceException;
    public Object get(Orhm id) throws PersistenceException;
    public Map<Object,Object> getAll(Orhm id) throws PersistenceException;
    public Object lock(Orhm id)throws PersistenceException;
    public void unlock(Orhm id)throws PersistenceException;

}
