package org.daum.library.replicatingMap.kevgen.JavaSENode
import org.kevoree.framework.port._
import org.daum.library.replicatingMap._
import scala.{Unit=>void}
class ReplicatingManagerPORTservice(component : ReplicatingManager) extends org.daum.library.replicatingMap.ReplicatingService with KevoreeProvidedPort {
def getName : String = "service"
def getComponentName : String = component.getName 
def getCache(name:java.lang.String) : org.daum.library.replicatingMap.Cache ={
val msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCache")
msgcall.getParams.put("name",name.asInstanceOf[AnyRef])
(this !? msgcall).asInstanceOf[org.daum.library.replicatingMap.Cache]}
override def internal_process(msg : Any)= msg match {
case opcall : org.kevoree.framework.MethodCallMessage => reply(opcall.getMethodName match {
case "getCache"=> component.getCache(if(opcall.getParams.containsKey("name")){opcall.getParams.get("name").asInstanceOf[java.lang.String]}else{opcall.getParams.get("arg0").asInstanceOf[java.lang.String]})
case _ @ o => println("uncatch message , method not found in service declaration : "+o);null 
})}
}
