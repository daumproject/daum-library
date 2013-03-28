package org.daum.library.replicav2.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 23/05/12
 * Time: 15:43
 */
public class Entry extends StoreMap<Object,Element>
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String name = "";

    public Element put(Object key, Element value) {

        try
        {
            Element e = get(key);
            if(e == null)
            {
                super.put(key,value);
            } else
            {

                switch (e.compare(value)  )
                {
                    case SIMULTANEOUS:

                        // conflict
                        System.out.println("Conflict");

                        break;
                    case SMALLER:
                        // update
                        System.out.println("Update");
                        super.put(key,value);
                        break;
                }
            }

            return null;

        }catch (Exception e )
        {

            return null;
        }
    }


    @Override
    public Element get(Object key) {
        Element versionedValue = (Element) super.get(key);
        return versionedValue;
    }


    @Override
    public Element remove(Object key) {

        return null;
    }

}
