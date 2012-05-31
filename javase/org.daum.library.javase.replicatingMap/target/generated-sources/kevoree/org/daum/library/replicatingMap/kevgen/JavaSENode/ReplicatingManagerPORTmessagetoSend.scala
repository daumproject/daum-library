package org.daum.library.replicatingMap.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.daum.library.replicatingMap._
class ReplicatingManagerPORTmessagetoSend(component : ReplicatingManager) extends org.kevoree.framework.MessagePort with KevoreeRequiredPort {
def getName : String = "messagetoSend"
def getComponentName : String = component.getName 
def process(o : Object) = {
{this ! o}
}
def getInOut = false
}
