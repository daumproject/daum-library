package org.daum.common.genmodel;

// TODO refactor
trait IModel {
  private var location : org.daum.common.genmodel.GpsPoint = null

  def getId : java.lang.String

  def getLocation : org.daum.common.genmodel.GpsPoint ={
    this.location
  }

  def setLocation(location : org.daum.common.genmodel.GpsPoint) {
    this.location = location
  }
}