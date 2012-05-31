package org.daum.library.fakeDemo.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.daum.library.fakeDemo._
class ReaderDaumPORTservice(component : ReaderDaum) extends org.daum.library.replicatingMap.ReplicatingService with KevoreeRequiredPort {
def getName : String = "service"
def getComponentName : String = component.getName 
def getInOut = true
def getCache(arg0:java.lang.String) : org.daum.library.replicatingMap.Cache ={
val msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCache")
msgcall.getParams.put("arg0",arg0.asInstanceOf[AnyRef])
(this !? msgcall).asInstanceOf[org.daum.library.replicatingMap.Cache]}
}
