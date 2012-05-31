package org.daum.library.replicatingMap.demo.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.replicatingMap.demo._
class ReplicatingProducteurFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ReplicatingProducteurFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ReplicatingProducteurFactory.remove(instanceName)
def createInstanceActivator = ReplicatingProducteurFactory.createInstanceActivator
}

object ReplicatingProducteurFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ReplicatingProducteurActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createReplicatingProducteur()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.demo.ReplicatingProducteur].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.demo.ReplicatingProducteur].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.demo.ReplicatingProducteur].update()}
}}
def createReplicatingProducteur() : org.daum.library.replicatingMap.demo.ReplicatingProducteur ={
val newcomponent = new org.daum.library.replicatingMap.demo.ReplicatingProducteur();
newcomponent.getNeededPorts().put("service",createReplicatingProducteurPORTservice(newcomponent))
newcomponent}
def createReplicatingProducteurPORTservice(component : ReplicatingProducteur) : ReplicatingProducteurPORTservice ={ return new ReplicatingProducteurPORTservice(component);}
}
