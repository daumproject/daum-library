package org.daum.library.javase.copterManager.utils;

import org.kevoree.ContainerRoot;
import org.kevoree.framework.KevoreePropertyHelper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/02/13
 * Time: 10:52
 */
public class WebHelper {


    public static List<String> getAddresses(ContainerRoot model, String remoteNodeName) {
        return KevoreePropertyHelper.getNetworkProperties(model, remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP());
    }


    public static String apply(ContainerRoot model, String nodename, String page) {
        // Maybe the behavior is not the best one (we use the first ip defined)
        return page.replace("$ip$", getAddresses(model, nodename).get(0));
    }
}
