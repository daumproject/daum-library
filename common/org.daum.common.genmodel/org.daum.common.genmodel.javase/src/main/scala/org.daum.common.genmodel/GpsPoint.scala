package org.daum.common.genmodel;


trait GpsPoint extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.Position {
  private var lat : java.lang.Integer = 0

  private var long : java.lang.Integer = 0

  private var satellites_used : java.lang.Integer = 0

  private var mode : java.lang.Integer = 0

  private var altitude : java.lang.Integer = 0

  override def toString: String = {
    ""+lat+" "+long;
  }

  def getLat : java.lang.Integer = {
    lat
  }

  def setLat(lat : java.lang.Integer) {
    this.lat = lat
  }


  def setLong(long : java.lang.Integer) {
    this.long = long
  }

  def getLong : java.lang.Integer = {
    long
  }

  def getSatellites_used : java.lang.Integer = {
    satellites_used
  }

  def setSatellites_used(satellites_used : java.lang.Integer) {
    this.satellites_used = satellites_used
  }

  def getMode : java.lang.Integer = {
    mode
  }

  def setMode(mode : java.lang.Integer) {
    this.mode = mode
  }

  def getAltitude : java.lang.Integer = {
    altitude
  }

  def setAltitude(altitude : java.lang.Integer) {
    this.altitude = altitude
  }


}
