package org.daum.library.ehcache.kevgen.JavaSENode
import org.kevoree.framework.port._
import org.daum.library.ehcache._
import scala.{Unit=>void}
class ehcacheManagerPORTehcacheChannel(component : ehcacheManager) extends org.kevoree.framework.MessagePort with KevoreeProvidedPort {
def getName : String = "ehcacheChannel"
def getComponentName : String = component.getName 
def process(o : Object) = {this ! o}
override def internal_process(msg : Any)= msg match {
case _ @ msg =>try{component.confCluster(msg)}catch{case _ @ e => {e.printStackTrace();println("Uncatched exception while processing Kevoree message")}}
}
}
