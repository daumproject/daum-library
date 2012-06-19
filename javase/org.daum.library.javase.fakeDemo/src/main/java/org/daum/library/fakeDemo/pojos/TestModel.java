package org.daum.library.fakeDemo.pojos;

import org.daum.library.ormH.annotations.Id;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 13:11
 */
public class TestModel implements Serializable {

    @Id()
    int id;
    int sum = 0;

    public  TestModel(int id){
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }


    public int getId() {
        return id;
    }
}
