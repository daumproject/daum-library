package org.daum.library.replicatingMap.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.replicatingMap._
class ReplicatingManagerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ReplicatingManagerFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ReplicatingManagerFactory.remove(instanceName)
def createInstanceActivator = ReplicatingManagerFactory.createInstanceActivator
}

object ReplicatingManagerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ReplicatingManagerActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createReplicatingManager()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.ReplicatingManager].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.ReplicatingManager].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.ReplicatingManager].update()}
}}
def createReplicatingManager() : org.daum.library.replicatingMap.ReplicatingManager ={
val newcomponent = new org.daum.library.replicatingMap.ReplicatingManager();
newcomponent.getHostedPorts().put("remoteReceived",createReplicatingManagerPORTremoteReceived(newcomponent))
newcomponent.getHostedPorts().put("service",createReplicatingManagerPORTservice(newcomponent))
newcomponent.getNeededPorts().put("messagetoSend",createReplicatingManagerPORTmessagetoSend(newcomponent))
newcomponent.getNeededPorts().put("trigger",createReplicatingManagerPORTtrigger(newcomponent))
newcomponent}
def createReplicatingManagerPORTremoteReceived(component : ReplicatingManager) : ReplicatingManagerPORTremoteReceived ={ new ReplicatingManagerPORTremoteReceived(component)}
def createReplicatingManagerPORTservice(component : ReplicatingManager) : ReplicatingManagerPORTservice ={ new ReplicatingManagerPORTservice(component)}
def createReplicatingManagerPORTmessagetoSend(component : ReplicatingManager) : ReplicatingManagerPORTmessagetoSend ={ return new ReplicatingManagerPORTmessagetoSend(component);}
def createReplicatingManagerPORTtrigger(component : ReplicatingManager) : ReplicatingManagerPORTtrigger ={ return new ReplicatingManagerPORTtrigger(component);}
}
