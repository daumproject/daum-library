package org.daum.library.ormH.persistence;
import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.annotations.ManyToOne;
import org.daum.library.ormH.annotations.OneToMany;
import org.daum.library.ormH.utils.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Field field;
    private String cacheName = "";
    private Field f_ManyToOne = null;
    private Field f_OneToMany = null;

    private static Logger logger =  LoggerFactory.getLogger(PersistentProperty.class);


    public PersistentProperty(PersistentClass persistentClass, Field field) {
        this.pers = persistentClass;
        this.field = field;
        parse();
    }

    public Field getF_ManyToOne() {
        return f_ManyToOne;
    }

    public void setF_ManyToOne(Field f_ManyToOne) {
        this.f_ManyToOne = f_ManyToOne;
    }

    public Field getF_OneToMany() {
        return f_OneToMany;
    }

    public void setF_OneToMany(Field f_OneToMany) {
        this.f_OneToMany = f_OneToMany;
    }

    public void parse()
    {
        Id id = null;
        ManyToOne manyToOne = null;
        OneToMany oneToMany = null;
        Generated generatedValue = null;
        id = field.getAnnotation(Id.class);

        if (id != null)
        {
            setId(true);
            field.setAccessible(true);
            pers.setPersistentPropertyID(this);
            setCacheName(pers.getClazz().getName());
            setName(field.getName());
            generatedValue = field.getAnnotation(Generated.class);
            if(generatedValue != null)
            {
                generatedType = generatedValue.strategy();
            }
        }

        manyToOne = field.getAnnotation(ManyToOne.class);
        if(manyToOne != null)
        {
            f_ManyToOne = field;
            field.setAccessible(true);

            /* foreignKey
             */
        }

        oneToMany = field.getAnnotation(OneToMany.class);
        if(oneToMany != null)
        {

            field.setAccessible(true);
            f_OneToMany = field;
        }


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

    public Field getField() {
        return field;
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
            return  getField().get(bean);
        } catch (Exception e) {
            throw new PersistenceException("getValue "+ getField().getName()+" "+e.getCause());
        }
    }

    public void setValue(Object bean, Object value) throws PersistenceException
    {
        try
        {
            getField().set(bean,value);
        } catch (IllegalAccessException e) {
            throw new PersistenceException("setValue "+ getField().getName()+" "+e.getCause());
        }
    }
}