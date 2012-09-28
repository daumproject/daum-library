package org.kevoree.nativeN;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/09/12
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class Generator {

    private LinkedHashMap<String,Integer> inputs_ports = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String,Integer> ouputs_ports = new LinkedHashMap<String, Integer>();
    private String pathfile = "";

    public String getPathfile() {
        return pathfile;
    }

    public void setPathfile(String pathfile) {
        this.pathfile = pathfile;
    }

    public LinkedHashMap<String, Integer> getInputs_ports() {
        return inputs_ports;
    }

    public LinkedHashMap<String, Integer> getOuputs_ports() {
        return ouputs_ports;
    }


    public String generateStepPreCompile(){
        StringBuilder gen = new StringBuilder();
        gen.append(generateOutputsPorts());

        gen.append(generateInputsPorts());

        gen.append(generateMethods());

        return gen.toString();
    }

    public String generateInputsPorts()
    {
        StringBuilder gen = new StringBuilder();
        for (String name : inputs_ports.keySet()){
            gen.append("\n/* @Port(name = \""+name+"\") */\n");
            gen.append("void "+name+"(void *input) {\n");
            gen.append("// USE INPUT\n");
            gen.append("}\n");
        }
        return gen.toString();
    }

    public String generateMethods(){
        StringBuilder gen =new StringBuilder();

        gen.append("\n" +
                "/*@Start*/\n" +
                "int start()\n" +
                "{\n" +
                "\tfprintf(stderr,\"Component starting \\n\");\n" +
                "\n" +
                "\n" +
                "}\n" +
                "\n" +
                "/*@Stop */\n" +
                "int stop()\n" +
                "{\n" +
                "    fprintf(stderr,\"Component stoping \\n\");\n" +
                "\n" +
                "}\n" +
                "\n" +
                "/*@Update */\n" +
                "int update()\n" +
                "{\n" +
                "    fprintf(stderr,\"Component updating \\n\");\n" +
                " \n" +
                "}\n");

        return gen.toString();
    }

    public String generateOutputsPorts(){

        StringBuilder gen = new StringBuilder();
        for (String name : ouputs_ports.keySet()){

            gen.append("void "+name+"(void *input);\n");
        }

        return gen.toString();
    }

    public String generateStepCompile(){
        StringBuilder gen = new StringBuilder();

        for (String name : ouputs_ports.keySet()){
            gen.append("void "+name+"(void *input) {\n");
            gen.append(" process_output("+ouputs_ports.get(name)+",input);\n");
            gen.append("}\n");

        }

        gen.append("void dispatch(int port,int id_queue) {\n");
        gen.append(" kmessage *msg = dequeue(id_queue);\n");
        gen.append("  if(msg !=NULL)  {\n\n" +
                "    switch(port)\n\n" +
                "    {\n");

        for (String name : inputs_ports.keySet()){
            gen.append("\t\t\t case "+inputs_ports.get(name)+":\n");
            gen.append("\t\t\t\t\t "+name+"(msg->value);\n");
            gen.append("\t\t\t break;\n");
        }
        gen.append("   }\n" +
                "   }\n" +
                "}\n");


        gen.append("int main (int argc,char *argv[])\n" +
                "{\n" +
                "   \tif(argc >2)\n" +
                "    {\n" +
                "\t    key_t key =   atoi(argv[1]);\n" +
                "\t    int port=   atoi(argv[2]);\n" +
                "\n" +
                "\t     bootstrap(key,port);\n" +
                "         register_start(start);\n" +
                "         register_stop(stop);\n" +
                "         register_update(update);\n" +
                "         register_dispatch(dispatch);\n" +
                "\t     ctx->start();\n" +
                "\n" +
                "\twhile(1)\n" +
                "\t{\n" +
                "        sleep(1000000);\n" +
                "\t}\n" +
                "     }\n" +
                "}");

        return gen.toString();
    }



    public int create_input(String name)
    {
        inputs_ports.put(name,inputs_ports.size());
        return inputs_ports.size();
    }

    public int create_output(String name)
    {
        ouputs_ports.put(name,ouputs_ports.size());
        return ouputs_ports.size();
    }


}
