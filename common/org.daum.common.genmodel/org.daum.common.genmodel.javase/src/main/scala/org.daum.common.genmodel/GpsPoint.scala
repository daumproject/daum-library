package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait GpsPoint extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.Position {
  private var lat : java.lang.Integer = 0

  private var long : java.lang.Integer = 0

  private var satellites_used : java.lang.Integer = 0

  private var mode : java.lang.Integer = 0

  private var altitude : java.lang.Integer = 0


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
  override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createGpsPoint
    selfObjectClone.setLat(this.getLat)
    selfObjectClone.setLong(this.getLong)
    selfObjectClone.setSatellites_used(this.getSatellites_used)
    selfObjectClone.setMode(this.getMode)
    selfObjectClone.setAltitude(this.getAltitude)
    subResult.put(this,selfObjectClone)
  }
  override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : GpsPoint = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.GpsPoint]
    clonedSelfObject
  }

}
