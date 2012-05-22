package org.daum.library.ehcache.kevgen.AndroidNode;
import java.util.Hashtable
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService
import org.kevoree.framework.KevoreeActor
import org.kevoree.framework.KevoreeComponent
import org.kevoree.framework._
import org.daum.library.ehcache._
class ehcacheManagerActivator extends org.kevoree.framework.osgi.KevoreeComponentActivator {
def callFactory() : KevoreeComponent = { ehcacheManagerFactory.createComponentActor() } }
