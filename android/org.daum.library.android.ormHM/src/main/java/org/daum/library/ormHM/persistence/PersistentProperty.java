package org.daum.library.ormHM.persistence;
import org.daum.library.ormHM.annotations.Generated;
import org.daum.library.ormHM.annotations.Id;
import org.daum.library.ormHM.utils.PersistenceException;

import java.lang.reflect.Field;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 15/05/12
 * Time: 19:00
 */
public class PersistentProperty {

    private PersistentClass pers;
    private String name;
    private Class clazz;
    private boolean id=false;
    private int generatedType = GeneratedType.MANUEL;
    private Field fieldID;
    private String cacheName = "";

    public PersistentProperty(PersistentClass persistentClass, Field fieldID) {
        this.pers = persistentClass;
        this.fieldID = fieldID;
        parse();
    }

    public void parse()
    {
        Id id = null;
        Generated generatedValue = null;
        id = fieldID.getAnnotation(Id.class);
        if (id != null)
        {
            setId(true);
            fieldID.setAccessible(true);
            pers.setPersistentPropertyID(this);
            setCacheName(pers.getClazz().getName());
            setName(fieldID.getName());
            generatedValue = fieldID.getAnnotation(Generated.class);
            if(generatedValue != null)
            {
                generatedType = generatedValue.strategy();
            }
        }



        // todo parse
    }


    public int getGeneratedType() {
        return generatedType;
    }

    public void setGeneratedType(int generatedType) {
        this.generatedType = generatedType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public PersistentClass getPers() {
        return pers;
    }
    public void setPers(PersistentClass pers) {
        this.pers = pers;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Class getClazz() {
        return clazz;
    }
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Field getFieldID() {
        return fieldID;
    }

    public boolean isId() {
        return id;
    }
    public void setId(boolean id) {
        this.id = id;
    }

    public Object getValue(Object bean) throws PersistenceException {
        try
        {
            return  getFieldID().get(bean);
        } catch (Exception e) {
            throw new PersistenceException("getValue "+ getFieldID().getName()+" "+e.getCause());
        }
    }

    public void setValue(Object bean, Object value) throws PersistenceException
    {
        try
        {
            getFieldID().set(bean,value);
        } catch (IllegalAccessException e) {
            throw new PersistenceException("setValue "+ getFieldID().getName()+" "+e.getCause());
        }
    }
}