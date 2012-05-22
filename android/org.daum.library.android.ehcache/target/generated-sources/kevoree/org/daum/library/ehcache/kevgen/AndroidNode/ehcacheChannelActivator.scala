package org.daum.library.ehcache.kevgen.AndroidNode;
import java.util.Hashtable
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService
import org.kevoree.framework.KevoreeActor
import org.kevoree.framework._
import org.kevoree.framework.KevoreeComponent
class ehcacheChannelActivator extends org.kevoree.framework.osgi.KevoreeChannelFragmentActivator {
def callFactory() : org.kevoree.framework.KevoreeChannelFragment = { ehcacheChannelFactory.createChannel() } }
