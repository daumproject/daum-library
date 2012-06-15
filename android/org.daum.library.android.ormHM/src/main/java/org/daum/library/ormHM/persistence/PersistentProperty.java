package org.daum.library.ormHM.persistence;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.daum.library.ormHM.annotations.GeneratedValue;
import org.daum.library.ormHM.annotations.Id;
import org.daum.library.ormHM.utils.PersistenceException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
    private  GenerationType generationType= GenerationType.MANUEL;
    private Field att;
    private String cacheName = "";


    public PersistentProperty(PersistentClass persistentClass, Field att) {
        this.pers = persistentClass;
        this.att = att;
        parse();
    }

    public void parse()
    {
        Id id = null;
        GeneratedValue generatedValue = null;
        id = att.getAnnotation(Id.class);
        if (id != null)
        {
            setId(true);
            pers.setId(this);
            setCacheName(pers.getClazz().getName());
            setName(att.getName());
            generatedValue = att.getAnnotation(GeneratedValue.class);
            if(generatedValue != null)
            {
                generationType  = generatedValue.strategy();
            }
        }
    }


    public GenerationType getGenerationType() {
        return generationType;
    }

    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
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


    public boolean isId() {
        return id;
    }
    public void setId(boolean id) {
        this.id = id;
    }

    public Object getValue(Object bean) throws PersistenceException {
        try {
            return PropertyUtils.getSimpleProperty(bean, getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(Object bean, Object value) throws PersistenceException
    {
        Object valueConverted = ConvertUtils.convert(value, getClazz());
        try {
            PropertyUtils.setSimpleProperty(bean, getName(), valueConverted);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}