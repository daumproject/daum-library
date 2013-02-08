package org.daum.library.android.daumauth.util

import org.kevoree.{ContainerNode, ComponentInstance, ContainerRoot}


class GenerateModelHelper() {

  def findChannel (componentName: String, portName: String, nodeName: String, currentModel: ContainerRoot): Option[String] = {
  /*  currentModel.getMBindings.find(b => b.getPort.getPortTypeRef.getName == portName && b.getPort.eContainer.asInstanceOf[ComponentInstance].getName == componentName &&
      b.getPort.eContainer.eContainer.asInstanceOf[ContainerNode].getName == nodeName) match {
      case None => None
      case Some(binding) => {
        Some(binding.getHub.getName)
      }
    } */

    Some("")
  }
}
