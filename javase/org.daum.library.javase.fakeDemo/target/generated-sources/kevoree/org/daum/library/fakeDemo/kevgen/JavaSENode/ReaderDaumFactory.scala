package org.daum.library.fakeDemo.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.fakeDemo._
class ReaderDaumFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ReaderDaumFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ReaderDaumFactory.remove(instanceName)
def createInstanceActivator = ReaderDaumFactory.createInstanceActivator
}

object ReaderDaumFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ReaderDaumActivator
def createComponentActor() : KevoreeComponent = {
	new KevoreeComponent(createReaderDaum()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.ReaderDaum].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.ReaderDaum].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.fakeDemo.ReaderDaum].update()}
}}
def createReaderDaum() : org.daum.library.fakeDemo.ReaderDaum ={
val newcomponent = new org.daum.library.fakeDemo.ReaderDaum();
newcomponent.getHostedPorts().put("notify",createReaderDaumPORTnotify(newcomponent))
newcomponent.getNeededPorts().put("service",createReaderDaumPORTservice(newcomponent))
newcomponent.getNeededPorts().put("value",createReaderDaumPORTvalue(newcomponent))
newcomponent}
def createReaderDaumPORTnotify(component : ReaderDaum) : ReaderDaumPORTnotify ={ new ReaderDaumPORTnotify(component)}
def createReaderDaumPORTservice(component : ReaderDaum) : ReaderDaumPORTservice ={ return new ReaderDaumPORTservice(component);}
def createReaderDaumPORTvalue(component : ReaderDaum) : ReaderDaumPORTvalue ={ return new ReaderDaumPORTvalue(component);}
}
