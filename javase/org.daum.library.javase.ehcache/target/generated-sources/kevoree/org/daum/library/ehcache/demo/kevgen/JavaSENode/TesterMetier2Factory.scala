package org.daum.library.ehcache.demo.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.ehcache.demo._
class TesterMetier2Factory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=TesterMetier2Factory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=TesterMetier2Factory.remove(instanceName)
def createInstanceActivator = TesterMetier2Factory.createInstanceActivator}
object TesterMetier2Factory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new TesterMetier2Activator
def createComponentActor() : KevoreeComponent = {
new KevoreeComponent(createTesterMetier2()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.demo.TesterMetier2].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.demo.TesterMetier2].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.demo.TesterMetier2].update()}
}}
def createTesterMetier2() : org.daum.library.ehcache.demo.TesterMetier2 ={
var newcomponent = new org.daum.library.ehcache.demo.TesterMetier2();
newcomponent.getNeededPorts().put("ehCacheService",createTesterMetier2PORTehCacheService(newcomponent))
newcomponent}
def createTesterMetier2PORTehCacheService(component : TesterMetier2) : TesterMetier2PORTehCacheService ={ return new TesterMetier2PORTehCacheService(component);}
}
