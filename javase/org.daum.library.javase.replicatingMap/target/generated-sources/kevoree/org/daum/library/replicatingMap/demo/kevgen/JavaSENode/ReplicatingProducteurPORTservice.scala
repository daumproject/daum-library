package org.daum.library.replicatingMap.demo.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.daum.library.replicatingMap.demo._
class ReplicatingProducteurPORTservice(component : ReplicatingProducteur) extends org.daum.library.replicatingMap.ReplicatingService with KevoreeRequiredPort {
def getName : String = "service"
def getComponentName : String = component.getName 
def getInOut = true
def getCache(name:java.lang.String) : org.daum.library.replicatingMap.Cache ={
val msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCache")
msgcall.getParams.put("name",name.asInstanceOf[AnyRef])
(this !? msgcall).asInstanceOf[org.daum.library.replicatingMap.Cache]}
}
