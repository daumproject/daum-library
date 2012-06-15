package model.sitac;

import org.daum.library.ormHM.annotations.GeneratedValue;
import org.daum.library.ormHM.annotations.Id;
import org.daum.library.ormHM.persistence.GenerationType;

import java.io.Serializable;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 16/05/12
 * Time: 13:11
 */
public class TestModel implements Serializable {

    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
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
