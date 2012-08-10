package org.daum.common.genmodel;

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

  @OneToMany
  private var moyen : org.daum.common.genmodel.Moyens = null

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

  def getMoyen : org.daum.common.genmodel.Moyens = {
    moyen
  }

  def setMoyen(moyen : org.daum.common.genmodel.Moyens ) {
    this.moyen = (moyen)

  }

}
