package org.daum.common.genmodel;


trait InfoPos extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoTactic {
  private var position : org.daum.common.genmodel.Position = _


  def getPosition : org.daum.common.genmodel.Position = {
    position
  }

  def setPosition(position : org.daum.common.genmodel.Position ) {
    if(this.position!= position) {
      this.position = (position)
    }
  }

}
