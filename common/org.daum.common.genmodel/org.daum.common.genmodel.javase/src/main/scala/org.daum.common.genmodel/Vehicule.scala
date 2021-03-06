/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/07/12
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
package org.daum.common.genmodel;

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType

trait Vehicule extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.IModel with org.daum.common.genmodel.Materiel {

  private var vehiculetype : org.daum.common.genmodel.VehiculeType = null
  private var gh_desengagement : java.util.Date = null
  // number is 01 in FPT01
  private var number : java.lang.String = null
  private var cis : java.lang.String = null
  private var gh_demande : java.util.Date = null
  private var gh_depart : java.util.Date = null
  private var gh_crm : java.util.Date = null
  private var gh_engage : java.util.Date = null

  def getGh_desengagement : java.util.Date ={
    gh_desengagement
  }

  def getGh_depart : java.util.Date ={
    gh_depart
  }

  def getGh_demande : java.util.Date ={
    gh_demande
  }

  def getGh_crm : java.util.Date ={
    gh_crm
  }

  def getGh_engage : java.util.Date ={
    gh_engage
  }

  def getNumber : java.lang.String ={
    number
  }

  def getCis : java.lang.String ={
    cis
  }

  def setGh_desengagement(date : java.util.Date) {
    this.gh_desengagement = date
  }

  def setGh_depart(date : java.util.Date) {
    this.gh_depart = date
  }

  def setGh_demande(date : java.util.Date) {
    this.gh_demande = date
  }

  def setGh_crm(date : java.util.Date) {
    this.gh_crm = date
  }

  def setGh_engage(date : java.util.Date) {
    this.gh_engage = date
  }

  def setNumber(number : java.lang.String) {
    this.number = number
  }

  def setCis(cis : java.lang.String) {
    this.cis = cis
  }

  def getVehiculeType : org.daum.common.genmodel.VehiculeType = {
    vehiculetype
  }

  def setVehiculeType(vehiculetype : org.daum.common.genmodel.VehiculeType) {
    this.vehiculetype = vehiculetype
  }
}



