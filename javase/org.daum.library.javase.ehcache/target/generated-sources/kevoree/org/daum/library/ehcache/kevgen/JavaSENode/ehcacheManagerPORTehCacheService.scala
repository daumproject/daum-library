package org.daum.library.ehcache.kevgen.JavaSENode
import org.kevoree.framework.port._
import org.daum.library.ehcache._
import scala.{Unit=>void}
class ehcacheManagerPORTehCacheService(component : ehcacheManager) extends org.daum.library.ehcache.IehcacheService with KevoreeProvidedPort {
def getName : String = "ehCacheService"
def getComponentName : String = component.getName 
def getCacheManger() : net.sf.ehcache.CacheManager ={
val msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCacheManger")
(this !? msgcall).asInstanceOf[net.sf.ehcache.CacheManager]}
override def internal_process(msg : Any)= msg match {
case opcall : org.kevoree.framework.MethodCallMessage => reply(opcall.getMethodName match {
case "getCacheManger"=> component.getCacheManger()
case _ @ o => println("uncatch message , method not found in service declaration : "+o);null 
})}
}
