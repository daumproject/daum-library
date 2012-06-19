package org.daum.library.demos;

import org.daum.library.ormHM.annotations.Generated;
import org.daum.library.ormHM.annotations.Id;
import org.daum.library.ormHM.persistence.GeneratedType;

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
