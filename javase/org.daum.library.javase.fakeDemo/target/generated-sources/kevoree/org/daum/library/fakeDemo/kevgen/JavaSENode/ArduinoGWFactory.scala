package org.daum.library.fakeDemo.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.fakeDemo._
class ArduinoGWFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ArduinoGWFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ArduinoGWFactory.remove(instanceName)
def createInstanceActivator = ArduinoGWFactory.createInstanceActivator
}

object ArduinoGWFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ArduinoGWActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createArduinoGW()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.ArduinoGW].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.ArduinoGW].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.ArduinoGW].update()}
}}
def createArduinoGW() : org.daum.library.fakeDemo.ArduinoGW ={
val newcomponent = new org.daum.library.fakeDemo.ArduinoGW();
newcomponent.getHostedPorts().put("inputvalue",createArduinoGWPORTinputvalue(newcomponent))
newcomponent.getNeededPorts().put("service",createArduinoGWPORTservice(newcomponent))
newcomponent}
def createArduinoGWPORTinputvalue(component : ArduinoGW) : ArduinoGWPORTinputvalue ={ new ArduinoGWPORTinputvalue(component)}
def createArduinoGWPORTservice(component : ArduinoGW) : ArduinoGWPORTservice ={ return new ArduinoGWPORTservice(component);}
}
