package org.daum.common.genmodel

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType


trait MessageAmbiance extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
	private var id : java.lang.String = ""
  private var jeSuis : java.lang.String = null
  private var jeVois : java.lang.String = null
  private var jePrevois : java.lang.String = null
  private var jeFais : java.lang.String = null
  private var jeDemande : java.lang.String = null
  private var groupeHoraire : java.util.Date = null
  private var sender : java.lang.String = null


  def getJeSuis : java.lang.String = {
    jeSuis
  }

  def setJeSuis(jeSuis : java.lang.String) {
    this.jeSuis = jeSuis
  }

  def getJeVois : java.lang.String = {
    jeVois
  }

  def setJeVois(jeVois : java.lang.String) {
    this.jeVois = jeVois
  }

  def getId : java.lang.String = {
    id
  }

  def setId(id : java.lang.String) {
    this.id = id
  }

  def getJePrevois : java.lang.String = {
    jePrevois
  }

  def setJePrevois(jePrevois : java.lang.String) {
    this.jePrevois = jePrevois
  }

  def getJeFais : java.lang.String = {
    jeFais
  }

  def setJeFais(jeFais : java.lang.String) {
    this.jeFais = jeFais
  }

  def getJeDemande : java.lang.String = {
    jeDemande
  }

  def setJeDemande(jeDemande : java.lang.String) {
    this.jeDemande = jeDemande
  }

  def getGroupeHoraire : java.util.Date = {
    groupeHoraire
  }

  def setGroupeHoraire(groupeHoraire : java.util.Date) {
    this.groupeHoraire = groupeHoraire
  }

  def getSender : java.lang.String = {
    sender
  }

  def setSender(sender : java.lang.String) {
    this.sender = sender
  }

  /**
   * @return a well formed string representation of the message
   */
  def getText : java.lang.String ={
    jeSuis+"\n"+jeVois+"\n"+jePrevois+"\n"+ jeFais +"\n"+jeDemande
  }

  /**
   * @return true if every fields are empty; false otherwise
   */
  def isEmpty : java.lang.Boolean ={
    (jeSuis+jeVois+jePrevois+jeFais+jeDemande).isEmpty
  }

  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
      val selfObjectClone = SitacFactory.createMessageAmbiance
      selfObjectClone.setJeSuis(this.getJeSuis)
      selfObjectClone.setJeVois(this.getJeVois)
      selfObjectClone.setId(this.getId)
      subResult.put(this,selfObjectClone)
    }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : MessageAmbiance = {
      val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.MessageAmbiance]
      clonedSelfObject
    }

}
