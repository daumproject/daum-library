package org.daum.library.sensors;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;


@Library(name = "JavaSE", names = {"Android"})
@Provides({
        @ProvidedPort(name = "consume", type = PortType.MESSAGE,theadStrategy = ThreadStrategy.NONE)
})
@DictionaryType({
        @DictionaryAttribute(name = "duree", defaultValue = "1", optional = true)
})
@ComponentType
public class HelloConsumerComponent extends AbstractComponentType {

    private int counter=0;
    private double start;
    @Start
    public void startComponent() {
        System.out.println("Consumer:: Start");
        start = System.currentTimeMillis();
    }

    @Stop
    public void stopComponent() {
        System.out.println("Consumer:: Stop");
    }

    @Update
    public void updateComponent() {
        System.out.println("Consumer:: Update");
    }

    @Port(name = "consume")
    public void consumeHello(Object o)
    {
        double duree = (System.currentTimeMillis() - start)  / 1000;
        counter++;
        if(duree > Long.valueOf((String) getDictionary().get("duree")))
        {
            System.out.println(counter+";"+duree);
            counter = 0;
            start = System.currentTimeMillis();
        }

        if(o instanceof Data) {
            Data msg = (Data)o;
        //    System.out.println("HelloConsumer received: " + msg);
        }
    }

}
