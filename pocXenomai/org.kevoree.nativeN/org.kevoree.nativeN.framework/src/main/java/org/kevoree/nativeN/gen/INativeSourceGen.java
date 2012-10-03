package org.kevoree.nativeN.gen;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 03/10/12
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public interface INativeSourceGen {

    public LinkedHashMap<String, Integer> getInputs_ports();
    public LinkedHashMap<String, Integer> getOuputs_ports();

}
