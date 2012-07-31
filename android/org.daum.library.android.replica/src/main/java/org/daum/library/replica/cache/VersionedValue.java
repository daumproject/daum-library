package org.daum.library.replica.cache;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 07/06/12
 * Time: 10:38
 */
public class VersionedValue implements java.io.Serializable, java.lang.Cloneable {
    private static final long serialVersionUID = 1515L;
    private long version = 0;
    private java.lang.Object object;
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void updated(){
          version++;
    }

    public boolean compareV(Object o) {
        if(o instanceof VersionedValue){
            VersionedValue s = (VersionedValue)o;
            if(s.getVersion() == this.getVersion()){
                return true;
            }  else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object obj) {
        this.object = obj;
    }
}
