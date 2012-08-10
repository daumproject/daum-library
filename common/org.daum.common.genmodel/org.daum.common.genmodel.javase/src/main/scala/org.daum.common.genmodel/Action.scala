package org.daum.common.genmodel;


trait Action extends org.daum.common.genmodel.SitacContainer with IModel with InfoLignePos with InfoHorodate with InfoNiveau {

  private var noria : java.lang.Boolean = false
  private var enLigne : java.lang.Boolean = false
  private var `type` : org.daum.common.genmodel.ActionType = null
  private var points : java.util.List[GpsPoint] = null

  def getNoria : java.lang.Boolean = {
    noria
  }

  def setNoria(noria : java.lang.Boolean) {
    this.noria = noria
  }

  def getEnLigne : java.lang.Boolean = {
    enLigne
  }

  def setEnLigne(enLigne : java.lang.Boolean) {
    this.enLigne = enLigne
  }

  def getType : org.daum.common.genmodel.ActionType = {
    `type`
  }

  def setType(`type` : org.daum.common.genmodel.ActionType ) {
    this.`type` = (`type`)

  }

  def getPoints : java.util.List[GpsPoint] ={
    this.points
  }

  def setPoints(pts : java.util.List[GpsPoint]) {
    this.points = pts
  }
}
