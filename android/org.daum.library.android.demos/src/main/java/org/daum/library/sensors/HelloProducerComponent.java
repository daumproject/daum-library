package org.daum.library.sensors;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;



@Library(name = "JavaSE", names = {"Android"})
@Requires({
        @RequiredPort(name = "produce", type = PortType.MESSAGE, optional = true,theadStrategy = ThreadStrategy.SHARED_THREAD)
})
@DictionaryType({
        @DictionaryAttribute(name = "helloProductionDelay", defaultValue = "2000", optional = true)
})
@ComponentType
public class HelloProducerComponent extends AbstractComponentType implements HelloProductionListener {

    private HelloProducerThread producer;
    private Data data;
    @Start
    public void startComponent()
    {
        data = new Data();
        if (producer == null || producer.isStopped()) {
            producer = new HelloProducerThread(Long.valueOf((String) getDictionary().get("helloProductionDelay")));
            producer.addHelloProductionListener(this);
            producer.start();
        }
    }

    @Stop
    public void stopComponent() {
        if (producer != null) {
            producer.halt();
        }
    }

    @Update
    public void updateComponent() {
        stopComponent();
        startComponent();
    }

    public void helloProduced(String helloValue) {
        MessagePort prodPort = getPortByName("produce",MessagePort.class);
        if(prodPort != null)
        {
            prodPort.process(data);
        }
    }
}
