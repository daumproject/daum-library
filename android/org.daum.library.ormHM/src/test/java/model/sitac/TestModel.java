package model.sitac;

import org.daum.library.ormHM.annotations.Id;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 13:11
 */
public class TestModel implements Serializable {
    
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

    @Id(attachTOCache = "TestModel")
    public int getId() {
        return id;
    }
}
