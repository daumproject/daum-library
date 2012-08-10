package org.daum.common.genmodel;


import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany
import java.util.ArrayList


trait Moyens extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var id : String = ""
  private var precision : java.lang.String = ""


  @OneToMany
  private lazy val agents : java.util.ArrayList[org.daum.common.genmodel.Agent] = new ArrayList[org.daum.common.genmodel.Agent]()


  @OneToMany
  private lazy val materiels : java.util.ArrayList[org.daum.common.genmodel.Materiel] = new ArrayList[org.daum.common.genmodel.Materiel]()


  private var posRef : org.daum.common.genmodel.Position = null

  private var posTarget : org.daum.common.genmodel.Position = null


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
    this.materiels.add(materiel)
  }


  def getAgents : java.util.List[org.daum.common.genmodel.Agent] = {
    agents
  }
  def getAgentsForJ : java.util.List[org.daum.common.genmodel.Agent] = {
    agents
  }

  def setAgents(Agents : java.util.List[org.daum.common.genmodel.Agent] ) {
    this.agents.clear()
    this.agents.addAll(Agents)

  }

  def addAgent(Agents : org.daum.common.genmodel.Agent) {
    this.agents.add(Agents)
  }

  def addAllAgents(Agents : java.util.List[org.daum.common.genmodel.Agent]) {
    Agents.addAll(agents)
  }

  def removeAgents(Agents : org.daum.common.genmodel.Agent) {
    if(this.agents.size != 0 && this.agents.indexOf(Agents) != -1 ) {
      this.agents.remove(this.agents.indexOf(Agents))
    }
  }

  def removeAllAgents() {
    this.agents.clear()
  }

  def getPosRef : org.daum.common.genmodel.Position = {
    posRef
  }

  def setPosRef(posRef : org.daum.common.genmodel.Position ) {
    if(this.posRef!= posRef){
      this.posRef = (posRef)
    }

  }

  def getPosTarget : org.daum.common.genmodel.Position = {
    posTarget
  }

  def setPosTarget(posTarget : org.daum.common.genmodel.Position ) {
    if(this.posTarget!= posTarget){
      this.posTarget = (posTarget)
 }

  }

}
