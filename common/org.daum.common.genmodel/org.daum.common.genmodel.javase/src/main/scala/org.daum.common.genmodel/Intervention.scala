package org.daum.common.genmodel;


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

  private var description : java.lang.String = ""

  private var code : org.daum.common.genmodel.CodeSinistre = _

  private var historique : org.daum.common.genmodel.Historique = null

  private var position : org.daum.common.genmodel.Position = null

  @ManyToOne
  private var requerant : org.daum.common.genmodel.Personne = null

  @OneToMany
  private lazy val detachements : java.util.ArrayList[org.daum.common.genmodel.Detachement] = new java.util.ArrayList[org.daum.common.genmodel.Detachement]()

  @OneToMany
  private lazy val victimes : java.util.ArrayList[org.daum.common.genmodel.Personne] = new java.util.ArrayList[org.daum.common.genmodel.Personne]()

  @OneToMany
  private lazy val infoTactics : java.util.ArrayList[org.daum.common.genmodel.InfoTactic] = new java.util.ArrayList[org.daum.common.genmodel.InfoTactic]()


  def getNumeroIntervention : java.lang.String = {
    numeroIntervention
  }

  def setNumeroIntervention(numeroIntervention : java.lang.String) {
    this.numeroIntervention = numeroIntervention
  }

  def getDescription : java.lang.String = {
    description
  }

  def setDescription(description : java.lang.String) {
    this.description = description
  }


  def getCode : org.daum.common.genmodel.CodeSinistre = {
    code
  }

  def setType(code : org.daum.common.genmodel.CodeSinistre ) {
    this.code = (code)

  }

  def getHistorique : org.daum.common.genmodel.Historique = {
    historique
  }

  def setHistorique(historique : org.daum.common.genmodel.Historique ) {
    if(this.historique!= historique){
      this.historique = (historique)
      }

  }

  def getPosition : org.daum.common.genmodel.Position = {
    position
  }

  def setPosition(position : org.daum.common.genmodel.Position ) {
    if(this.position!= position){
      this.position = (position)
      }

  }

  def getRequerant : org.daum.common.genmodel.Personne = {
    requerant
  }

  def setRequerant(requerant : org.daum.common.genmodel.Personne ) {
    this.requerant = (requerant)

  }

  def getDetachements : java.util.List[org.daum.common.genmodel.Detachement] = {
    detachements
  }
  def getDetachementsForJ : java.util.List[org.daum.common.genmodel.Detachement] = {
    detachements
  }

  def setDetachements(detachements : java.util.List[org.daum.common.genmodel.Detachement] ) {
    if(this.detachements!= detachements){
      this.detachements.clear()
      this.detachements.addAll(detachements)
    }
  }

  def addDetachements(detachements : org.daum.common.genmodel.Detachement) {
    this.detachements.add(detachements)
  }

  def addAllDetachements(detachements : java.util.List[org.daum.common.genmodel.Detachement]) {
    detachements.addAll(detachements)
  }

  def removeDetachements(detachements : org.daum.common.genmodel.Detachement) {
    if(this.detachements.size != 0 && this.detachements.indexOf(detachements) != -1 ) {
      this.detachements.remove(this.detachements.indexOf(detachements))
    }
  }

  def removeAllDetachements() {
    this.detachements.clear()
  }

  def getVictimes : java.util.List[org.daum.common.genmodel.Personne] = {
    victimes
  }
  def getVictimesForJ : java.util.List[org.daum.common.genmodel.Personne] = {
    import scala.collection.JavaConversions._
    victimes
  }

  def setVictimes(victimes : java.util.List[org.daum.common.genmodel.Personne] ) {
    this.victimes.clear()
    this.victimes.addAll(victimes)

  }

  def addVictimes(victimes : org.daum.common.genmodel.Personne) {
    this.victimes.add(victimes)
  }

  def addAllVictimes(victimes : java.util.List[org.daum.common.genmodel.Personne]) {
    victimes.addAll(victimes)
  }

  def removeVictimes(victimes : org.daum.common.genmodel.Personne) {
    if(this.victimes.size != 0 && this.victimes.indexOf(victimes) != -1 ) {
      this.victimes.remove(this.victimes.indexOf(victimes))
    }
  }

  def removeAllVictimes() {
    this.victimes.clear()
  }

  def getInfoTactics : java.util.List[org.daum.common.genmodel.InfoTactic] = {
    infoTactics
  }
  def getInfoTacticsForJ : java.util.List[org.daum.common.genmodel.InfoTactic] = {
    infoTactics
  }

  def setInfoTactics(infoTactics : java.util.List[org.daum.common.genmodel.InfoTactic] ) {
    if(this.infoTactics!= infoTactics){
      this.infoTactics.clear()
      this.infoTactics.addAll(infoTactics)
    }
  }

  def addInfoTactics(infoTactics : org.daum.common.genmodel.InfoTactic) {
    this.infoTactics.add(infoTactics)
  }

  def addAllInfoTactics(infoTactics : java.util.List[org.daum.common.genmodel.InfoTactic]) {
    infoTactics.addAll(infoTactics)
  }

  def removeInfoTactics(infoTactics : org.daum.common.genmodel.InfoTactic) {
    if(this.infoTactics.size != 0 && this.infoTactics.indexOf(infoTactics) != -1 ) {
      this.infoTactics.remove(this.infoTactics.indexOf(infoTactics))
    }
  }

  def removeAllInfoTactics() {
    this.infoTactics.clear()
  }
}
