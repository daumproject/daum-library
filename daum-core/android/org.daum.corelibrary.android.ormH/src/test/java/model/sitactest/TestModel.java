package model.sitactest;

import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 13:11
 */
public class TestModel implements Serializable {

    @Id()
    @Generated(strategy = GeneratedType.UUID)
    private  String id ="";
    int sum = 0;

    public  TestModel(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
    public String getId() {
        return id;
    }
}
