package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany


trait Affectation extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var id : String = ""

  private var horodatageDemande : java.util.Date=_
  private var horodatageEngagement : java.util.Date=_
  private var horodatageDesengagement : java.util.Date=_
  private var moyen : Option[org.daum.common.genmodel.Moyens] = null

  def getId : java.lang.String = {
    id
  }

  def getHorodatageDemande : java.util.Date = {
    horodatageDemande
  }

  def setHorodatageDemande(horodatageDemande : java.util.Date) {
    this.horodatageDemande = horodatageDemande
  }

  def getHorodatageEngagement : java.util.Date = {
    horodatageEngagement
  }

  def setHorodatageEngagement(horodatageEngagement : java.util.Date) {
    this.horodatageEngagement = horodatageEngagement
  }

  def getHorodatageDesengagement : java.util.Date = {
    horodatageDesengagement
  }

  def setHorodatageDesengagement(horodatageDesengagement : java.util.Date) {
    this.horodatageDesengagement = horodatageDesengagement
  }

  def getMoyen : Option[org.daum.common.genmodel.Moyens] = {
    moyen
  }

  def setMoyen(moyen : Option[org.daum.common.genmodel.Moyens] ) {
    this.moyen = (moyen)

  }
  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createAffectation
    selfObjectClone.setHorodatageDemande(this.getHorodatageDemande)
    selfObjectClone.setHorodatageEngagement(this.getHorodatageEngagement)
    selfObjectClone.setHorodatageDesengagement(this.getHorodatageDesengagement)
    subResult.put(this,selfObjectClone)
  }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Affectation = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Affectation]
    this.getMoyen.map{sub =>
      clonedSelfObject.setMoyen(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Moyens]))
    }

    clonedSelfObject
  }

}
