package org.daum.library.replicatingMap.demo.kevgen.JavaSENode;
import java.util.Hashtable
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService
import org.kevoree.framework.KevoreeActor
import org.kevoree.framework.KevoreeComponent
import org.kevoree.framework._
import org.daum.library.replicatingMap.demo._
class ReplicatingProducteurActivator extends org.kevoree.framework.osgi.KevoreeComponentActivator {
def callFactory() : KevoreeComponent = { ReplicatingProducteurFactory.createComponentActor() } }
