package org.daum.library.ehcache.kevgen.JavaSENode
import org.kevoree.framework._
import org.daum.library.ehcache._
class ehcacheManagerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ehcacheManagerFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ehcacheManagerFactory.remove(instanceName)
def createInstanceActivator = ehcacheManagerFactory.createInstanceActivator}
object ehcacheManagerFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ehcacheManagerActivator
def createComponentActor() : KevoreeComponent = {
new KevoreeComponent(createehcacheManager()){def startComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.ehcacheManager].start()}
def stopComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.ehcacheManager].stop()}
override def updateComponent(){getKevoreeComponentType.asInstanceOf[org.daum.library.ehcache.ehcacheManager].update()}
}}
def createehcacheManager() : org.daum.library.ehcache.ehcacheManager ={
var newcomponent = new org.daum.library.ehcache.ehcacheManager();
newcomponent.getHostedPorts().put("ehcacheChannel",createehcacheManagerPORTehcacheChannel(newcomponent))
newcomponent.getHostedPorts().put("ehCacheService",createehcacheManagerPORTehCacheService(newcomponent))
newcomponent}
def createehcacheManagerPORTehcacheChannel(component : ehcacheManager) : ehcacheManagerPORTehcacheChannel ={ new ehcacheManagerPORTehcacheChannel(component)}
def createehcacheManagerPORTehCacheService(component : ehcacheManager) : ehcacheManagerPORTehCacheService ={ new ehcacheManagerPORTehCacheService(component)}
}
