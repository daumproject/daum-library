package org.daum.library.javase.sleepycat.pojo;

import com.sleepycat.persist.model.*;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/03/13
 * Time: 09:03
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Employee {

    @PrimaryKey
    public Integer id;
    public String name;
    public String forname;

    @SecondaryKey(relate = Relationship.MANY_TO_MANY,
            relatedEntity = Employee.class,
            onRelatedEntityDelete = DeleteAction.NULLIFY)
    public Set<Long> projects;

    public Employee(Integer id, String name, String forname, Set<Long> projects) {
        this.id = id;
        this.name = name;
        this.forname = forname;
        this.projects = projects;
    }
}

