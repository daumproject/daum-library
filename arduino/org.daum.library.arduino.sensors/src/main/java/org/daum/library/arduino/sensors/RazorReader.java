package org.daum.library.arduino.sensors;

import org.kevoree.annotation.*;
import org.kevoree.tools.arduino.framework.AbstractArduinoComponent;
import org.kevoree.tools.arduino.framework.AbstractPeriodicArduinoComponent;
import org.kevoree.tools.arduino.framework.ArduinoGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 14/08/12
 * Time: 09:18
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Arduino")
@ComponentType
@Requires({
        @RequiredPort(name = "razor", type = PortType.MESSAGE, optional = true)
})
public class RazorReader  extends AbstractPeriodicArduinoComponent {


    @Override
    public void generateHeader(ArduinoGenerator gen) {
        gen.appendNativeStatement("char buf_razor [50];");
        gen.appendNativeStatement("int c=0;");
    }


    @Override
    public void generateInit(ArduinoGenerator gen)
    {
        gen.appendNativeStatement("Serial1.begin(115200);");
    }

    @Override
    public void generatePeriodic(ArduinoGenerator gen) {


           gen.appendNativeStatement("  if (Serial1.available()) \n" +
                   "  {\n" +
                   "    char inByte = Serial1.read();\n" +
                   "    \n" +
                   "    \n" +
                   "    if(inByte == '\\n'){");


        // data is available in the buffer
        gen.appendNativeStatement("kmessage * smsg;");

        gen.appendNativeStatement("smsg = (kmessage*) malloc(sizeof(kmessage));");
        gen.appendNativeStatement("if (smsg){memset(smsg, 0, sizeof(kmessage));}");
        gen.appendNativeStatement("smsg->value = buf_razor;\n");
        gen.appendNativeStatement("smsg->metric=\"imu\";");
        gen.appendNativeStatement("razor_rport(smsg);");

        gen.appendNativeStatement("          c = 0;\n" +
                "    }else \n" +
                "    {\n" +
                "      buf_razor[c] = inByte;\n" +
                "        c++;\n" +
                "    }\n" +
                "  }");


    }
}
