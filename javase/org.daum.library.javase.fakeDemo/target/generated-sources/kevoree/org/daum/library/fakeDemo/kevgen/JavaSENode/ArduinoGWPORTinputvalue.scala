package org.daum.library.fakeDemo.kevgen.JavaSENode
import org.kevoree.framework.port._
import org.daum.library.fakeDemo._
import scala.{Unit=>void}
class ArduinoGWPORTinputvalue(component : ArduinoGW) extends org.kevoree.framework.MessagePort with KevoreeProvidedPort {
def getName : String = "inputvalue"
def getComponentName : String = component.getName 
def process(o : Object) = {this ! o}
override def internal_process(msg : Any)= msg match {
case _ @ msg =>try{component.inputvalue(msg)}catch{case _ @ e => {e.printStackTrace();println("Uncatched exception while processing Kevoree message")}}
}
}
