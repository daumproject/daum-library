package org.kevoree.android.bluetooth

/**
 * Created by IntelliJ IDEA.
 * User: duke
 * Date: 09/02/12
 * Time: 12:53
 */

import java.lang.Class
import org.kevoree.framework.AbstractChannelFragment
import scala.collection.JavaConversions._
import org.kevoree.framework.aspects.KevoreeAspects._
import org.slf4j.LoggerFactory

class ChannelClassResolver (asbChannelFrag: AbstractChannelFragment) {
  def resolve (className: String): Class[_] = {
    println("tryRoResolveClass=" + className)

    val model = asbChannelFrag.getModelService.getLastModel
    val currentNode = model.getNodes.find(n => n.getName == asbChannelFrag.getNodeName).get
    var resolvedClass: Class[_] = null
    asbChannelFrag.getBindedPorts.foreach {
      bport =>
        if (resolvedClass == null) {
          currentNode.getComponents.find(c => c.getName == bport.getComponentName) match {
            case Some(component) => {
              val du = component.getTypeDefinition.foundRelevantDeployUnit(currentNode)
              if (du != null) {
                val kcl = asbChannelFrag.getBootStrapperService.getKevoreeClassLoaderHandler.getKevoreeClassLoader(du)
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
      resolvedClass = asbChannelFrag.getClass.getClassLoader.loadClass(className)
    }
    if (resolvedClass == null) {
      LoggerFactory.getLogger(this.getClass).error("Fail to resolve class=" + className)
    } else {
      println("Resolved," + className + "-" + resolvedClass)
    }
    resolvedClass
  }
}

