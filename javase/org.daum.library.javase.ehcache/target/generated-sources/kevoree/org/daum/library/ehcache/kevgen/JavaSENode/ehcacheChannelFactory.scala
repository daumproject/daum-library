package org.daum.library.ehcache.kevgen.JavaSENode
import org.kevoree.framework._
class ehcacheChannelFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
override def registerInstance(instanceName : String, nodeName : String)=ehcacheChannelFactory.registerInstance(instanceName,nodeName)
override def remove(instanceName : String)=ehcacheChannelFactory.remove(instanceName)
def createInstanceActivator = ehcacheChannelFactory.createInstanceActivator}
object ehcacheChannelFactory extends org.kevoree.framework.osgi.KevoreeInstanceFactory {
def createInstanceActivator: org.kevoree.framework.osgi.KevoreeInstanceActivator = new ehcacheChannelActivator
def createChannel()={new org.daum.library.ehcache.ehcacheChannel with ChannelTypeFragment {
override def startChannelFragment(){this.asInstanceOf[org.daum.library.ehcache.ehcacheChannel].startChannel()}
override def stopChannelFragment(){this.asInstanceOf[org.daum.library.ehcache.ehcacheChannel].stopChannel()}
override def updateChannelFragment(){this.asInstanceOf[org.daum.library.ehcache.ehcacheChannel].updateChannel()}
}}}
