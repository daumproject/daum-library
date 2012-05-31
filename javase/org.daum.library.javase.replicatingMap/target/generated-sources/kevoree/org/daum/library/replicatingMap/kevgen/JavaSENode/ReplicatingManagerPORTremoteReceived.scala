package org.daum.library.replicatingMap.kevgen.JavaSENode
import org.kevoree.framework.port._
import org.daum.library.replicatingMap._
import scala.{Unit=>void}
class ReplicatingManagerPORTremoteReceived(component : ReplicatingManager) extends org.kevoree.framework.MessagePort with KevoreeProvidedPort {
def getName : String = "remoteReceived"
def getComponentName : String = component.getName 
def process(o : Object) = {this ! o}
override def internal_process(msg : Any)= msg match {
case _ @ msg =>try{component.messageReceived(msg)}catch{case _ @ e => {e.printStackTrace();println("Uncatched exception while processing Kevoree message")}}
}
}
