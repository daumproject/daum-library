package org.daum.common.genmodel;


trait SourceDanger extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.IModel with org.daum.common.genmodel.InfoPos {

  private var dangerType : DangerType = DangerType.FIRE

  def getType : DangerType ={
    this.dangerType
  }

  def setType(dangerType : DangerType) {
    this.dangerType = dangerType
  }



}
