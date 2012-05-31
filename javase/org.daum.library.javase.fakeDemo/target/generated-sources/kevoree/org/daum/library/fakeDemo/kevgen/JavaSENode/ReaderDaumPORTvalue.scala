package org.daum.library.fakeDemo.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.daum.library.fakeDemo._
class ReaderDaumPORTvalue(component : ReaderDaum) extends org.kevoree.framework.MessagePort with KevoreeRequiredPort {
def getName : String = "value"
def getComponentName : String = component.getName 
def process(o : Object) = {
{this ! o}
}
def getInOut = false
}
