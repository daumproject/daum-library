package org.daum.library.fakeDemo.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.fakeDemo._
class GeneratorDaumFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=GeneratorDaumFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=GeneratorDaumFactory.remove(instanceName)
def createInstanceActivator = GeneratorDaumFactory.createInstanceActivator
}

object GeneratorDaumFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new GeneratorDaumActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createGeneratorDaum()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.GeneratorDaum].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.GeneratorDaum].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.GeneratorDaum].update()}
}}
def createGeneratorDaum() : org.daum.library.fakeDemo.GeneratorDaum ={
val newcomponent = new org.daum.library.fakeDemo.GeneratorDaum();
newcomponent.getNeededPorts().put("service",createGeneratorDaumPORTservice(newcomponent))
newcomponent}
def createGeneratorDaumPORTservice(component : GeneratorDaum) : GeneratorDaumPORTservice ={ return new GeneratorDaumPORTservice(component);}
}
