package org.daum.library.ormHM.annotations;

import org.daum.library.ormHM.persistence.GenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/06/12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {
    GenerationType strategy() default GenerationType.UUID;
}
