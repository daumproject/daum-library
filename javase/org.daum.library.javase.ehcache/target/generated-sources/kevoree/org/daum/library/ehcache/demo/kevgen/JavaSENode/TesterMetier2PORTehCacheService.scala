package org.daum.library.ehcache.demo.kevgen.JavaSENode
import org.kevoree.framework.port._
import scala.{Unit=>void}
import org.daum.library.ehcache.demo._
class TesterMetier2PORTehCacheService(component : TesterMetier2) extends org.daum.library.ehcache.IehcacheService with KevoreeRequiredPort {
def getName : String = "ehCacheService"
def getComponentName : String = component.getName 
def getInOut = true
def getCacheManger() : net.sf.ehcache.CacheManager ={
var msgcall = new org.kevoree.framework.MethodCallMessage
msgcall.setMethodName("getCacheManger");
(this !? msgcall).asInstanceOf[net.sf.ehcache.CacheManager]}
}
