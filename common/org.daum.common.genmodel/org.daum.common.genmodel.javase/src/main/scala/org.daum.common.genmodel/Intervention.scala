package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */

import java.io.Serializable
import java.lang.String
import java.util.Date
import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany
import org.daum.library.ormH.annotations.ManyToOne

trait Intervention extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var numeroIntervention : java.lang.String = ""

  private var code : org.daum.common.genmodel.CodeSinistre = _

  private var historique : Option[org.daum.common.genmodel.Historique] = null

  private var position : Option[org.daum.common.genmodel.Position] = null

  @ManyToOne
  private var requerant : Option[org.daum.common.genmodel.Personne] = null

  @OneToMany
  private lazy val detachements : scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Detachement] = new scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Detachement]()

  @OneToMany
  private lazy val victimes : scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Personne] = new scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Personne]()

  @OneToMany
  private lazy val infoTactics : scala.collection.mutable.ListBuffer[org.daum.common.genmodel.InfoTactic] = new scala.collection.mutable.ListBuffer[org.daum.common.genmodel.InfoTactic]()


  def getNumeroIntervention : java.lang.String = {
    numeroIntervention
  }

  def setNumeroIntervention(numeroIntervention : java.lang.String) {
    this.numeroIntervention = numeroIntervention
  }


  def getCode : org.daum.common.genmodel.CodeSinistre = {
    code
  }

  def setType(code : org.daum.common.genmodel.CodeSinistre ) {
    this.code = (code)

  }

  def getHistorique : Option[org.daum.common.genmodel.Historique] = {
    historique
  }

  def setHistorique(historique : Option[org.daum.common.genmodel.Historique] ) {
    if(this.historique!= historique){
      this.historique = (historique)
      historique.map{ dic=>				dic.setEContainer(this, Some(() => { this.historique= null }) )
      }}

  }

  def getPosition : Option[org.daum.common.genmodel.Position] = {
    position
  }

  def setPosition(position : Option[org.daum.common.genmodel.Position] ) {
    if(this.position!= position){
      this.position = (position)
      position.map{ dic=>				dic.setEContainer(this, Some(() => { this.position= null }) )
      }}

  }

  def getRequerant : Option[org.daum.common.genmodel.Personne] = {
    requerant
  }

  def setRequerant(requerant : Option[org.daum.common.genmodel.Personne] ) {
    this.requerant = (requerant)

  }

  def getDetachements : List[org.daum.common.genmodel.Detachement] = {
    detachements.toList
  }
  def getDetachementsForJ : java.util.List[org.daum.common.genmodel.Detachement] = {
    import scala.collection.JavaConversions._
    detachements
  }

  def setDetachements(detachements : List[org.daum.common.genmodel.Detachement] ) {
    if(this.detachements!= detachements){
      this.detachements.clear()
      this.detachements.insertAll(0,detachements)
      detachements.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removeDetachements(el)}))
      }
    }

  }

  def addDetachements(detachements : org.daum.common.genmodel.Detachement) {
    detachements.setEContainer(this,Some(()=>{this.removeDetachements(detachements)}))
    this.detachements.append(detachements)
  }

  def addAllDetachements(detachements : List[org.daum.common.genmodel.Detachement]) {
    detachements.foreach{ elem => addDetachements(elem)}
  }

  def removeDetachements(detachements : org.daum.common.genmodel.Detachement) {
    if(this.detachements.size != 0 && this.detachements.indexOf(detachements) != -1 ) {
      this.detachements.remove(this.detachements.indexOf(detachements))
      detachements.setEContainer(null,None)
    }
  }

  def removeAllDetachements() {
    this.detachements.foreach{ elem => removeDetachements(elem)}
  }

  def getVictimes : List[org.daum.common.genmodel.Personne] = {
    victimes.toList
  }
  def getVictimesForJ : java.util.List[org.daum.common.genmodel.Personne] = {
    import scala.collection.JavaConversions._
    victimes
  }

  def setVictimes(victimes : List[org.daum.common.genmodel.Personne] ) {
    this.victimes.clear()
    this.victimes.insertAll(0,victimes)

  }

  def addVictimes(victimes : org.daum.common.genmodel.Personne) {
    this.victimes.append(victimes)
  }

  def addAllVictimes(victimes : List[org.daum.common.genmodel.Personne]) {
    victimes.foreach{ elem => addVictimes(elem)}
  }

  def removeVictimes(victimes : org.daum.common.genmodel.Personne) {
    if(this.victimes.size != 0 && this.victimes.indexOf(victimes) != -1 ) {
      this.victimes.remove(this.victimes.indexOf(victimes))
    }
  }

  def removeAllVictimes() {
    this.victimes.foreach{ elem => removeVictimes(elem)}
  }

  def getInfoTactics : List[org.daum.common.genmodel.InfoTactic] = {
    infoTactics.toList
  }
  def getInfoTacticsForJ : java.util.List[org.daum.common.genmodel.InfoTactic] = {
    import scala.collection.JavaConversions._
    infoTactics
  }

  def setInfoTactics(infoTactics : List[org.daum.common.genmodel.InfoTactic] ) {
    if(this.infoTactics!= infoTactics){
      this.infoTactics.clear()
      this.infoTactics.insertAll(0,infoTactics)
      infoTactics.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removeInfoTactics(el)}))
      }
    }

  }

  def addInfoTactics(infoTactics : org.daum.common.genmodel.InfoTactic) {
    infoTactics.setEContainer(this,Some(()=>{this.removeInfoTactics(infoTactics)}))
    this.infoTactics.append(infoTactics)
  }

  def addAllInfoTactics(infoTactics : List[org.daum.common.genmodel.InfoTactic]) {
    infoTactics.foreach{ elem => addInfoTactics(elem)}
  }

  def removeInfoTactics(infoTactics : org.daum.common.genmodel.InfoTactic) {
    if(this.infoTactics.size != 0 && this.infoTactics.indexOf(infoTactics) != -1 ) {
      this.infoTactics.remove(this.infoTactics.indexOf(infoTactics))
      infoTactics.setEContainer(null,None)
    }
  }

  def removeAllInfoTactics() {
    this.infoTactics.foreach{ elem => removeInfoTactics(elem)}
  }
  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createIntervention
    selfObjectClone.setNumeroIntervention(this.getNumeroIntervention)
    subResult.put(this,selfObjectClone)
    this.getHistorique.map{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getPosition.map{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getDetachements.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getInfoTactics.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

  }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Intervention = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Intervention]
    clonedSelfObject.setType(addrs.get(this.code).asInstanceOf[org.daum.common.genmodel.CodeSinistre])

    this.getHistorique.map{sub =>
      clonedSelfObject.setHistorique(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Historique]))
    }

    this.getPosition.map{sub =>
      clonedSelfObject.setPosition(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Position]))
    }

    this.getRequerant.map{sub =>
      clonedSelfObject.setRequerant(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Personne]))
    }

    this.getDetachements.foreach{sub =>
      clonedSelfObject.addDetachements(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Detachement])
    }

    this.getVictimes.foreach{sub =>
      clonedSelfObject.addVictimes(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Personne])
    }

    this.getInfoTactics.foreach{sub =>
      clonedSelfObject.addInfoTactics(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.InfoTactic])
    }

    this.getHistorique.map{ sub =>
      sub.resolve(addrs)
    }

    this.getPosition.map{ sub =>
      sub.resolve(addrs)
    }

    this.getDetachements.foreach{ sub =>
      sub.resolve(addrs)
    }

    this.getInfoTactics.foreach{ sub =>
      sub.resolve(addrs)
    }

    clonedSelfObject
  }

}
