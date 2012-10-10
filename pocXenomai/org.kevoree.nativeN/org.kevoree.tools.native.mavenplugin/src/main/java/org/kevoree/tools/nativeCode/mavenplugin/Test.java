package org.kevoree.tools.nativeCode.mavenplugin;


import org.kevoree.nativeN.utils.FileManager;

public class Test {





    public static void main(String[] args) throws Exception {

        String bridge = new String(FileManager.load(NativeMojo.class.getClassLoader().getResourceAsStream("Bridge.template")));




        System.out.println(        bridge.replace("$PACKAGE$","org.kevoree.native;"));


        System.exit(0);
    }


}
