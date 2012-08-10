package org.daum.common.genmodel;


trait PositionCivil extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.Position {
  private var nomRue : java.lang.String = ""

  private var cp : java.lang.String = ""

  private var numeroRue : java.lang.String = ""

  private var pays : java.lang.String = ""


  override def toString: String = {
    ""+nomRue+" "+cp
  }

  def getNomRue : java.lang.String = {
    nomRue
  }

  def setNomRue(nomRue : java.lang.String) {
    this.nomRue = nomRue
  }

  def getCp : java.lang.String = {
    cp
  }

  def setCp(cp : java.lang.String) {
    this.cp = cp
  }

  def getNumeroRue : java.lang.String = {
    numeroRue
  }

  def setNumeroRue(numeroRue : java.lang.String) {
    this.numeroRue = numeroRue
  }

  def getPays : java.lang.String = {
    pays
  }

  def setPays(pays : java.lang.String) {
    this.pays = pays
  }


}
