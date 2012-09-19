package org.daum.library.arduino.sensors;

import org.kevoree.annotation.*;
import org.kevoree.tools.arduino.framework.AbstractArduinoComponent;
import org.kevoree.tools.arduino.framework.AbstractPeriodicArduinoComponent;
import org.kevoree.tools.arduino.framework.ArduinoGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 18/09/12
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Arduino")
@ComponentType
@Provides({
        @ProvidedPort(name = "heart", type = PortType.MESSAGE),
        @ProvidedPort(name = "temp", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "light", type = PortType.MESSAGE, optional = true),
        @RequiredPort(name = "tone", type = PortType.MESSAGE, optional = true)
})
public class Balise extends AbstractPeriodicArduinoComponent
{


    @Override
    public void generateHeader(ArduinoGenerator gen) {

    }

    @Override
    public void generateClassHeader(ArduinoGenerator gen){

    }

    @Override
    public void generateInit(ArduinoGenerator gen) {

    }


    @Port(name = "heart")
    public void inputRazor(Object o)
    {
        /*
        getGenerator().appendNativeStatement("int value = atoi(msg->value);");
        getGenerator().appendNativeStatement("if(value < 50){  ");


        getGenerator().declareStaticKMessage("msg2","t");
        getGenerator().appendNativeStatement("msg2->value = \"tick\";");
        getGenerator().sendKMessage("msg2","light");
        getGenerator().freeStaticKMessage("msg2");


        getGenerator().appendNativeStatement("}");    */

    }

    @Port(name = "temp")
    public void inputtemp(Object o)
    {
        getGenerator().appendNativeStatement("int value = atoi(msg->value);");

        getGenerator().appendNativeStatement("if(value > 27){  ");

        getGenerator().declareStaticKMessage("msg","t");
        getGenerator().appendNativeStatement("msg->value = \"tick\";");
        getGenerator().sendKMessage("msg","light");
        getGenerator().freeStaticKMessage("msg");
/*
        getGenerator().declareStaticKMessage("msg2","t");
        getGenerator().appendNativeStatement("msg2->value = \"tick\";");
        getGenerator().sendKMessage("msg2","tone");
        getGenerator().freeStaticKMessage("msg2");
  */
        getGenerator().appendNativeStatement("}");
    }

    @Override
    public void generatePeriodic(ArduinoGenerator arduinoGenerator) {

        /*getGenerator().appendNativeStatement(" " +
                "currentMillis = millis();\n" +
                "      if((currentMillis - prealarme_previousMillis) > trigger_prealarme)\n" +
                "      {\n" +
                "         prealarme_previousMillis = currentMillis;\n" +
                "         if(prealarme_nbMotion < 8){    \n" +
                "            digitalWrite(12, HIGH);  \n" +
                "               tone(8,500,500); \n" +
                "            prealarme_nbMotion=0;\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                prealarme_nbMotion=0;\n" +
                "          }\n" +
                "      }");   */
    }
}
