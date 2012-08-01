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
import scala.collection.mutable.ListBuffer


trait Moyens extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var id : String = ""
  private var precision : java.lang.String = ""


  @OneToMany
  private lazy val agents : ListBuffer[org.daum.common.genmodel.Agent] = new ListBuffer[org.daum.common.genmodel.Agent]()


  @OneToMany
  private lazy val materiels : ListBuffer[org.daum.common.genmodel.Materiel] = new ListBuffer[org.daum.common.genmodel.Materiel]()


  private var posRef : Option[org.daum.common.genmodel.Position] = null

  private var posTarget : Option[org.daum.common.genmodel.Position] = null


  def getPrecision : java.lang.String = {
    precision
  }

  def setPrecision(precision : java.lang.String) {
    this.precision = precision
  }

  def getId() : java.lang.String = {
    id
  }



  def getMaterielForJ : java.util.List[org.daum.common.genmodel.Materiel] = {
    import scala.collection.JavaConversions._
    materiels
  }


  def addMateriel(materiel : org.daum.common.genmodel.Materiel) {
    this.materiels.append(materiel)
  }


  def getAgents : List[org.daum.common.genmodel.Agent] = {
    agents.toList
  }
  def getAgentsForJ : java.util.List[org.daum.common.genmodel.Agent] = {
    import scala.collection.JavaConversions._
    agents
  }

  def setAgents(Agents : List[org.daum.common.genmodel.Agent] ) {
    this.agents.clear()
    this.agents.insertAll(0,Agents)

  }

  def addAgent(Agents : org.daum.common.genmodel.Agent) {
    this.agents.append(Agents)
  }

  def addAllAgents(Agents : List[org.daum.common.genmodel.Agent]) {
    Agents.foreach{ elem => addAgent(elem)}
  }

  def removeAgents(Agents : org.daum.common.genmodel.Agent) {
    if(this.agents.size != 0 && this.agents.indexOf(Agents) != -1 ) {
      this.agents.remove(this.agents.indexOf(Agents))
    }
  }

  def removeAllAgents() {
    this.agents.foreach{ elem => removeAgents(elem)}
  }

  def getPosRef : Option[org.daum.common.genmodel.Position] = {
    posRef
  }

  def setPosRef(posRef : Option[org.daum.common.genmodel.Position] ) {
    if(this.posRef!= posRef){
      this.posRef = (posRef)
      posRef.map{ dic=>	dic.setEContainer(this, Some(() => { this.posRef= null }) )
      }}

  }

  def getPosTarget : Option[org.daum.common.genmodel.Position] = {
    posTarget
  }

  def setPosTarget(posTarget : Option[org.daum.common.genmodel.Position] ) {
    if(this.posTarget!= posTarget){
      this.posTarget = (posTarget)
      posTarget.map{ dic=>				dic.setEContainer(this, Some(() => { this.posTarget= null }) )
      }}

  }

}
