package org.daum.library.replicatingMap.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.replicatingMap._
class ReplicatingProducteurFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ReplicatingProducteurFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ReplicatingProducteurFactory.remove(instanceName)
def createInstanceActivator = ReplicatingProducteurFactory.createInstanceActivator}
object ReplicatingProducteurFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ReplicatingProducteurActivator
def createComponentActor() : KevoreeComponent = {
new KevoreeComponent(createReplicatingProducteur()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.ReplicatingProducteur].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.ReplicatingProducteur].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.replicatingMap.ReplicatingProducteur].update()}
}}
def createReplicatingProducteur() : org.daum.library.replicatingMap.ReplicatingProducteur ={
var newcomponent = new org.daum.library.replicatingMap.ReplicatingProducteur();
newcomponent.getNeededPorts().put("service",createReplicatingProducteurPORTservice(newcomponent))
newcomponent}
def createReplicatingProducteurPORTservice(component : ReplicatingProducteur) : ReplicatingProducteurPORTservice ={ return new ReplicatingProducteurPORTservice(component);}
}
