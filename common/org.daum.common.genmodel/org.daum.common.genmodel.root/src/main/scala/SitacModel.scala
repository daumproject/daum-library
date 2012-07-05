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
import org.daum.library.ormH.annotations.ManyToOne

trait SitacModel extends org.sitac.SitacContainer {

  @OneToMany
  private lazy val interventionTypes : scala.collection.mutable.ListBuffer[org.sitac.InterventionType] = new scala.collection.mutable.ListBuffer[org.sitac.InterventionType]()
  @OneToMany
  private lazy val interventions : scala.collection.mutable.ListBuffer[org.sitac.Intervention] = new scala.collection.mutable.ListBuffer[org.sitac.Intervention]()
  @OneToMany
  private lazy val personnes : scala.collection.mutable.ListBuffer[org.sitac.Personne] = new scala.collection.mutable.ListBuffer[org.sitac.Personne]()
  @OneToMany
  private lazy val typeActions : scala.collection.mutable.ListBuffer[org.sitac.ActionType] = new scala.collection.mutable.ListBuffer[org.sitac.ActionType]()


  def getInterventionTypes : List[org.sitac.InterventionType] = {
    interventionTypes.toList
  }
  def getInterventionTypesForJ : java.util.List[org.sitac.InterventionType] = {
    import scala.collection.JavaConversions._
    interventionTypes
  }

  def setInterventionTypes(interventionTypes : List[org.sitac.InterventionType] ) {
    if(this.interventionTypes!= interventionTypes){
      this.interventionTypes.clear()
      this.interventionTypes.insertAll(0,interventionTypes)
      interventionTypes.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removeInterventionTypes(el)}))
      }
    }

  }

  def addInterventionTypes(interventionTypes : org.sitac.InterventionType) {
    interventionTypes.setEContainer(this,Some(()=>{this.removeInterventionTypes(interventionTypes)}))
    this.interventionTypes.append(interventionTypes)
  }

  def addAllInterventionTypes(interventionTypes : List[org.sitac.InterventionType]) {
    interventionTypes.foreach{ elem => addInterventionTypes(elem)}
  }

  def removeInterventionTypes(interventionTypes : org.sitac.InterventionType) {
    if(this.interventionTypes.size != 0 && this.interventionTypes.indexOf(interventionTypes) != -1 ) {
      this.interventionTypes.remove(this.interventionTypes.indexOf(interventionTypes))
      interventionTypes.setEContainer(null,None)
    }
  }

  def removeAllInterventionTypes() {
    this.interventionTypes.foreach{ elem => removeInterventionTypes(elem)}
  }

  def getInterventions : List[org.sitac.Intervention] = {
    interventions.toList
  }
  def getInterventionsForJ : java.util.List[org.sitac.Intervention] = {
    import scala.collection.JavaConversions._
    interventions
  }

  def setInterventions(interventions : List[org.sitac.Intervention] ) {
    if(this.interventions!= interventions){
      this.interventions.clear()
      this.interventions.insertAll(0,interventions)
      interventions.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removeInterventions(el)}))
      }
    }

  }

  def addInterventions(interventions : org.sitac.Intervention) {
    interventions.setEContainer(this,Some(()=>{this.removeInterventions(interventions)}))
    this.interventions.append(interventions)
  }

  def addAllInterventions(interventions : List[org.sitac.Intervention]) {
    interventions.foreach{ elem => addInterventions(elem)}
  }

  def removeInterventions(interventions : org.sitac.Intervention) {
    if(this.interventions.size != 0 && this.interventions.indexOf(interventions) != -1 ) {
      this.interventions.remove(this.interventions.indexOf(interventions))
      interventions.setEContainer(null,None)
    }
  }

  def removeAllInterventions() {
    this.interventions.foreach{ elem => removeInterventions(elem)}
  }

  def getPersonnes : List[org.sitac.Personne] = {
    personnes.toList
  }
  def getPersonnesForJ : java.util.List[org.sitac.Personne] = {
    import scala.collection.JavaConversions._
    personnes
  }

  def setPersonnes(personnes : List[org.sitac.Personne] ) {
    if(this.personnes!= personnes){
      this.personnes.clear()
      this.personnes.insertAll(0,personnes)
      personnes.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removePersonnes(el)}))
      }
    }

  }

  def addPersonnes(personnes : org.sitac.Personne) {
    personnes.setEContainer(this,Some(()=>{this.removePersonnes(personnes)}))
    this.personnes.append(personnes)
  }

  def addAllPersonnes(personnes : List[org.sitac.Personne]) {
    personnes.foreach{ elem => addPersonnes(elem)}
  }

  def removePersonnes(personnes : org.sitac.Personne) {
    if(this.personnes.size != 0 && this.personnes.indexOf(personnes) != -1 ) {
      this.personnes.remove(this.personnes.indexOf(personnes))
      personnes.setEContainer(null,None)
    }
  }

  def removeAllPersonnes() {
    this.personnes.foreach{ elem => removePersonnes(elem)}
  }

  def getTypeActions : List[org.sitac.ActionType] = {
    typeActions.toList
  }
  def getTypeActionsForJ : java.util.List[org.sitac.ActionType] = {
    import scala.collection.JavaConversions._
    typeActions
  }

  def setTypeActions(typeActions : List[org.sitac.ActionType] ) {
    if(this.typeActions!= typeActions){
      this.typeActions.clear()
      this.typeActions.insertAll(0,typeActions)
      typeActions.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removeTypeActions(el)}))
      }
    }

  }

  def addTypeActions(typeActions : org.sitac.ActionType) {
    typeActions.setEContainer(this,Some(()=>{this.removeTypeActions(typeActions)}))
    this.typeActions.append(typeActions)
  }

  def addAllTypeActions(typeActions : List[org.sitac.ActionType]) {
    typeActions.foreach{ elem => addTypeActions(elem)}
  }

  def removeTypeActions(typeActions : org.sitac.ActionType) {
    if(this.typeActions.size != 0 && this.typeActions.indexOf(typeActions) != -1 ) {
      this.typeActions.remove(this.typeActions.indexOf(typeActions))
      typeActions.setEContainer(null,None)
    }
  }

  def removeAllTypeActions() {
    this.typeActions.foreach{ elem => removeTypeActions(elem)}
  }
  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createSitacModel
    subResult.put(this,selfObjectClone)
    this.getInterventionTypes.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getInterventions.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getPersonnes.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getTypeActions.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

  }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SitacModel = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.SitacModel]
    this.getInterventionTypes.foreach{sub =>
      clonedSelfObject.addInterventionTypes(addrs.get(sub).asInstanceOf[org.sitac.InterventionType])
    }

    this.getInterventions.foreach{sub =>
      clonedSelfObject.addInterventions(addrs.get(sub).asInstanceOf[org.sitac.Intervention])
    }

    this.getPersonnes.foreach{sub =>
      clonedSelfObject.addPersonnes(addrs.get(sub).asInstanceOf[org.sitac.Personne])
    }

    this.getTypeActions.foreach{sub =>
      clonedSelfObject.addTypeActions(addrs.get(sub).asInstanceOf[org.sitac.ActionType])
    }

    this.getInterventionTypes.foreach{ sub =>
      sub.resolve(addrs)
    }

      this.getInterventions.foreach{ sub =>
      sub.resolve(addrs)
    }

    this.getPersonnes.foreach{ sub =>
      sub.resolve(addrs)
    }

    this.getTypeActions.foreach{ sub =>
      sub.resolve(addrs)
    }

    clonedSelfObject
  }

}
