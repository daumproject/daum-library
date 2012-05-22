package org.daum.library.ehcache.demo.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.ehcache.demo._
class TesterMetierFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=TesterMetierFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=TesterMetierFactory.remove(instanceName)
def createInstanceActivator = TesterMetierFactory.createInstanceActivator}
object TesterMetierFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new TesterMetierActivator
def createComponentActor() : KevoreeComponent = {
new KevoreeComponent(createTesterMetier()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.demo.TesterMetier].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.demo.TesterMetier].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.demo.TesterMetier].update()}
}}
def createTesterMetier() : org.daum.library.ehcache.demo.TesterMetier ={
var newcomponent = new org.daum.library.ehcache.demo.TesterMetier();
newcomponent.getNeededPorts().put("ehCacheService",createTesterMetierPORTehCacheService(newcomponent))
newcomponent}
def createTesterMetierPORTehCacheService(component : TesterMetier) : TesterMetierPORTehCacheService ={ return new TesterMetierPORTehCacheService(component);}
}
