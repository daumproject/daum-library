package org.daum.library.sensors;




import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;


@Library(name = "JavaSE", names = {"Android"})
@DictionaryType({
        @DictionaryAttribute(name = "duree", defaultValue = "5", optional = true) ,
        @DictionaryAttribute(name = "port", defaultValue = "8000", optional = true)
})
@ComponentType
public class HelloConsumerComponentDirect extends AbstractComponentType {

    private int counter=0;

    @Start
    public void startComponent() {
        int port =   Integer.parseInt(getDictionary().get("port").toString());
        System.out.println("Consumer:: Start "+port);


        P2pServer server = new P2pServer(port);
        Thread  t_server = new Thread(server);
        t_server.start();
    }

    @Stop
    public void stopComponent() {
        System.out.println("Consumer:: Stop");
    }

    @Update
    public void updateComponent() {
        System.out.println("Consumer:: Update");
    }


}
