package org.daum.common.genmodel


trait Cible extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.IModel with org.daum.common.genmodel.InfoPos {

  private var cibleType : CibleType = CibleType.WATER

  def getType : CibleType ={
    this.cibleType
  }

  def setType(cibleType : CibleType) {
    this.cibleType = cibleType
  }


}
