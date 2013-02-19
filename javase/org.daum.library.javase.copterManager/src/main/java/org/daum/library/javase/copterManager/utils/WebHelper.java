package org.daum.library.javase.copterManager.utils;

import org.daum.library.javase.copterManager.cache.MemCache;
import org.kevoree.ContainerRoot;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.NetworkHelper;
import scala.Option;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/02/13
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class WebHelper {


    public static String getAddress(ContainerRoot model,String remoteNodeName) {
        Option<String> ipOption = NetworkHelper.getAccessibleIP(KevoreePropertyHelper.getNetworkProperties(model, remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP()));
        if (ipOption.isDefined()) {
            return ipOption.get();
        } else {
            return "";
        }
    }



    public static  String apply(ContainerRoot model,String nodename,String page)
    {
        return   page.replace("$ip$", getAddress(model, nodename));
    }
}
