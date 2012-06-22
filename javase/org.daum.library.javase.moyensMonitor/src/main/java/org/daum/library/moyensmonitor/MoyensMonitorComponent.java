package org.daum.library.moyensmonitor;

import org.daum.library.moyensmonitor.view.MoyensMonitorFrame;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 22/06/12
 * Time: 09:45
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE")
@ComponentType
public class MoyensMonitorComponent extends AbstractComponentType {

    @Start
    public void start() {
        MoyensMonitorFrame frame = new MoyensMonitorFrame(getNodeName());
        frame.setVisible(true);
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }
}
