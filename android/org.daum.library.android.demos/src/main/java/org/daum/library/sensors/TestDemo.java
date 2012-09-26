package org.daum.library.sensors;

import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 18/06/12
 * Time: 10:50
 * To change this template use File | Settings | File Templates.
 */
public class TestDemo
{
    @Id
    @Generated(strategy = GeneratedType.UUID)
    private String test = "";

}
