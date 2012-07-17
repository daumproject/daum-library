package org.sitac;

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
import scala.collection.mutable.ListBuffer


trait Moyens extends org.sitac.SitacContainer {
  private var precision : java.lang.String = ""

  private var numero : java.lang.String = ""

  @OneToMany
  private lazy val agents : ListBuffer[org.sitac.Agent] = new ListBuffer[org.sitac.Agent]()


  @OneToMany
  private lazy val materiels : ListBuffer[org.sitac.Materiel] = new ListBuffer[org.sitac.Materiel]()


  private var posRef : Option[org.sitac.Position] = null

  private var posTarget : Option[org.sitac.Position] = null


  def getPrecision : java.lang.String = {
    precision
  }

  def setPrecision(precision : java.lang.String) {
    this.precision = precision
  }

  def getNumero : java.lang.String = {
    numero
  }

  def setNumero(numero : java.lang.String) {
    this.numero = numero
  }



  def getMaterielForJ : java.util.List[org.sitac.Materiel] = {
    import scala.collection.JavaConversions._
    materiels
  }


  def addMateriel(materiel : org.sitac.Materiel) {
    this.materiels.append(materiel)
  }


  def getAgents : List[org.sitac.Agent] = {
    agents.toList
  }
  def getAgentsForJ : java.util.List[org.sitac.Agent] = {
    import scala.collection.JavaConversions._
    agents
  }

  def setAgents(Agents : List[org.sitac.Agent] ) {
    this.agents.clear()
    this.agents.insertAll(0,Agents)

  }

  def addAgent(Agents : org.sitac.Agent) {
    this.agents.append(Agents)
  }

  def addAllAgents(Agents : List[org.sitac.Agent]) {
    Agents.foreach{ elem => addAgent(elem)}
  }

  def removeAgents(Agents : org.sitac.Agent) {
    if(this.agents.size != 0 && this.agents.indexOf(Agents) != -1 ) {
      this.agents.remove(this.agents.indexOf(Agents))
    }
  }

  def removeAllAgents() {
    this.agents.foreach{ elem => removeAgents(elem)}
  }

  def getPosRef : Option[org.sitac.Position] = {
    posRef
  }

  def setPosRef(posRef : Option[org.sitac.Position] ) {
    if(this.posRef!= posRef){
      this.posRef = (posRef)
      posRef.map{ dic=>	dic.setEContainer(this, Some(() => { this.posRef= null }) )
      }}

  }

  def getPosTarget : Option[org.sitac.Position] = {
    posTarget
  }

  def setPosTarget(posTarget : Option[org.sitac.Position] ) {
    if(this.posTarget!= posTarget){
      this.posTarget = (posTarget)
      posTarget.map{ dic=>				dic.setEContainer(this, Some(() => { this.posTarget= null }) )
      }}

  }

}
