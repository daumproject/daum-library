package org.daum.library.p2pSock

import java.lang.Class
import org.kevoree.framework.AbstractChannelFragment
import scala.collection.JavaConversions._
import org.kevoree.framework.kaspects.TypeDefinitionAspect

class ChannelClassResolver(nioChannel: AbstractChannelFragment) {

  private val typeDefinitionAspect = new TypeDefinitionAspect()

  def resolve(className: String): Class[_] = {
    val model = nioChannel.asInstanceOf[P2pSock].getModel
    val currentNode = model.getNodes.find(n => n.getName == nioChannel.getNodeName).get
    var resolvedClass: Class[_] = null
    nioChannel.getBindedPorts.foreach {
      bport =>
        if (resolvedClass == null) {
          currentNode.getComponents.find(c => c.getName == bport.getComponentName) match {
            case Some(component) => {
              val du = typeDefinitionAspect.foundRelevantDeployUnit(component.getTypeDefinition, currentNode)
              if (du != null) {
                val kcl = nioChannel.getBootStrapperService.getKevoreeClassLoaderHandler.getKevoreeClassLoader(du)
                try {
                  resolvedClass = kcl.loadClass(className)
                  return resolvedClass
                } catch {
                  case _ => //IGNORE
                }
              }
            }
            case _ =>
          }
        }
    }
    if (resolvedClass == null) {
      resolvedClass = nioChannel.getClass.getClassLoader.loadClass(className)
    }
    resolvedClass
  }
}
