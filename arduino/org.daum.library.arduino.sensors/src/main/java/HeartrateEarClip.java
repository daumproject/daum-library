/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/07/12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */

import org.kevoree.annotation.*;
import org.kevoree.tools.arduino.framework.AbstractArduinoComponent;
import org.kevoree.tools.arduino.framework.ArduinoGenerator;

@Library(name = "Arduino")
@ComponentType
@Requires({
        @RequiredPort(name = "heartrate", type = PortType.MESSAGE, needCheckDependency = false)
})
public class HeartrateEarClip extends AbstractArduinoComponent
{

    @Override
    public void generateHeader(ArduinoGenerator gen)
    {
        gen.appendNativeStatement("unsigned char pin = 13;\n" +
                "unsigned char counter=0;\n" +
                "unsigned int heart_rate=0;\n" +
                "unsigned long temp[21];\n" +
                "unsigned long sub=0;\n" +
                "volatile unsigned char state = LOW;\n" +
                "bool data_effect=true;\n" +
                "const int max_heartpluse_duty=2000;//you can change it follow your system's request.2000 meams 2 seconds. System return error if the duty overtrip 2 second.");
        gen.appendNativeStatement("char buf[10];");
    }

    @Override
    public void generateClassHeader(ArduinoGenerator gen) {
        gen.appendNativeStatement("\n" +
                "void interrupt()\n" +
                "{\n" +
                "    temp[counter]=millis();\n" +
                "    state = !state;    \n" +
                "   // Serial.println(counter,DEC);\n" +
                "    //Serial.println(temp[counter]);\n" +
                "    switch(counter)\n" +
                "      {\n" +
                "       case(0):\n" +
                "       sub=temp[counter]-temp[20];\n" +
                "     //  Serial.println(sub);\n" +
                "       break;\n" +
                "       default:\n" +
                "       sub=temp[counter]-temp[counter-1];\n" +
                "       //Serial.println(sub);\n" +
                "       break;\n" +
                "      }\n" +
                "    if(sub>max_heartpluse_duty)//set 2 seconds as max heart pluse duty\n" +
                "      {\n" +
                "        data_effect=0;//sign bit\n" +
                "        counter=0;\n" +
                "        \n" +
                "        Serial.println(-1);\n" +
                "        array_init();\n" +
                "       }\n" +
                "    if (counter==20&&data_effect)\n" +
                "    {\n" +
                "      counter=0;\n" +
                "      sum();\n" +
                "    }\n" +
                "    else if(counter!=20&&data_effect)\n" +
                "    counter++;\n" +
                "    else \n" +
                "    {\n" +
                "      counter=0;\n" +
                "      data_effect=1;\n" +
                "    }\n" +
                "}\n" +
                "void array_init()\n" +
                "{\n" +
                "  for(unsigned char i=0;i!=20;++i)\n" +
                "  {\n" +
                "    temp[i]=0;\n" +
                "  }\n" +
                "  temp[20]=millis();\n" +
                "}");

        gen.appendNativeStatement("void sum()//calculate the heart rate\n" +
                "{\n" +
                " if(data_effect)\n" +
                "    {\n" +
                "      heart_rate=1200000/(temp[20]-temp[0]);//60*20*1000/20_total_time \n");

        getGenerator().appendNativeStatement("kmessage * smsg = (kmessage*) malloc(sizeof(kmessage));");
        getGenerator().appendNativeStatement("if (smsg){memset(smsg, 0, sizeof(kmessage));}");
        getGenerator().appendNativeStatement("sprintf(buf,\"%d\",heart_rate);\n");
        getGenerator().appendNativeStatement("smsg->value = buf;\n");
        getGenerator().appendNativeStatement("smsg->metric=\"hearrate\";");
        getGenerator().appendNativeStatement("heartrate_rport(smsg);");
        getGenerator().appendNativeStatement("free(smsg);");

        gen.appendNativeStatement("    }\n" +
                "   data_effect=1;//sign bit\n" +
                "}");



    }

    @Override
    public void generateInit(ArduinoGenerator gen)
    {
        gen.appendNativeStatement(" array_init();\n" +
                "  Serial.println(\"Heart rate test begin.\");\n" +
                "  attachInterrupt(0, interrupt, RISING);//set interrupt 0,digital port 2");
    }




}
