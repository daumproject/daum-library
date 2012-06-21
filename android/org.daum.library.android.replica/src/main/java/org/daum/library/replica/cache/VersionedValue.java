package org.daum.library.replica.cache;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 10:38
 */
public class VersionedValue implements java.io.Serializable, java.lang.Cloneable {

    private static final long serialVersionUID = 1515L;

    private volatile long version;
    private java.lang.Object value;

    public VersionedValue(Object value)
    {
        version  =System.nanoTime();
        this.value =value;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        else if(!(o instanceof VersionedValue))
            return false;

        return  false;
    }

    public  boolean after(VersionedValue o)
    {
        if(version > o.getVersion())
        {
            return  true;
        } else
        {
            return false;
        }
    }

    public  boolean before(VersionedValue o)
    {
        if(version < o.getVersion())
        {
            return  true;
        } else
        {
            return false;
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
