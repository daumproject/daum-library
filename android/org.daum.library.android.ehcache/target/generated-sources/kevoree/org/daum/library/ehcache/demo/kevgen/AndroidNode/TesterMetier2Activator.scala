package org.daum.library.ehcache.demo.kevgen.AndroidNode;
import java.util.Hashtable
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService
import org.kevoree.framework.KevoreeActor
import org.kevoree.framework.KevoreeComponent
import org.kevoree.framework._
import org.daum.library.ehcache.demo._
class TesterMetier2Activator extends org.kevoree.framework.osgi.KevoreeComponentActivator {
def callFactory() : KevoreeComponent = { TesterMetier2Factory.createComponentActor() } }
