package org.sitac;

trait IModel {
  private var location : org.sitac.GpsPoint = null

  def getId : java.lang.String

  def getLocation : org.sitac.GpsPoint ={
    this.location
  }

  def setLocation(location : org.sitac.GpsPoint) {
    this.location = location
  }
}