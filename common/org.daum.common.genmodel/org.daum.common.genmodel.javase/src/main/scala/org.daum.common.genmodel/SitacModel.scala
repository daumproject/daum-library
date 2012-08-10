package org.daum.common.genmodel;


import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany
import org.daum.library.ormH.annotations.ManyToOne

trait SitacModel extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var id : java.lang.String = ""
  @OneToMany
  private lazy val interventionTypes : java.util.ArrayList[org.daum.common.genmodel.CodeSinistre] = new java.util.ArrayList[org.daum.common.genmodel.CodeSinistre]()
  @OneToMany
  private lazy val interventions : java.util.ArrayList[org.daum.common.genmodel.Intervention] = new java.util.ArrayList[org.daum.common.genmodel.Intervention]()
  @OneToMany
  private lazy val personnes : java.util.ArrayList[org.daum.common.genmodel.Personne] = new java.util.ArrayList[org.daum.common.genmodel.Personne]()
  @OneToMany
  private lazy val typeActions : java.util.ArrayList[org.daum.common.genmodel.ActionType] = new java.util.ArrayList[org.daum.common.genmodel.ActionType]()


  def getInterventionTypes : java.util.List[org.daum.common.genmodel.CodeSinistre] = {
    interventionTypes
  }
  def getInterventionTypesForJ : java.util.List[org.daum.common.genmodel.CodeSinistre] = {
    interventionTypes
  }

  def setInterventionTypes(interventionTypes : java.util.List[org.daum.common.genmodel.CodeSinistre] ) {
    if(this.interventionTypes!= interventionTypes){
      this.interventionTypes.clear()
      this.interventionTypes.addAll(interventionTypes)
    }
  }


  def getId() : java.lang.String = {
    id
  }


  def addInterventionTypes(interventionTypes : org.daum.common.genmodel.CodeSinistre) {
    this.interventionTypes.add(interventionTypes)
  }

  def addAllInterventionTypes(interventionTypes : java.util.List[org.daum.common.genmodel.CodeSinistre]) {
    interventionTypes.addAll(interventionTypes)
  }

  def removeInterventionTypes(interventionTypes : org.daum.common.genmodel.CodeSinistre) {
    if(this.interventionTypes.size != 0 ) {
      this.interventionTypes.remove(interventionTypes)
    }
  }

  def removeAllInterventionTypes() {
    this.interventionTypes.clear()
  }

  def getInterventions : java.util.List[org.daum.common.genmodel.Intervention] = {
    interventions
  }
  def getInterventionsForJ : java.util.List[org.daum.common.genmodel.Intervention] = {
    interventions
  }

  def setInterventions(interventions : java.util.List[org.daum.common.genmodel.Intervention] ) {
    if(this.interventions!= interventions){
      this.interventions.clear()
      this.interventions.addAll(interventions)
    }

  }

  def addInterventions(interventions : org.daum.common.genmodel.Intervention) {
    this.interventions.add(interventions)
  }

  def addAllInterventions(interventions : java.util.List[org.daum.common.genmodel.Intervention]) {
    interventions.addAll(interventions)
  }

  def removeIntervention(intervention : org.daum.common.genmodel.Intervention) {
    if(this.interventions.size != 0 ) {
      this.interventions.remove(intervention)
    }
  }

  def removeAllInterventions() {
    this.interventions.clear()
  }

  def getPersonnes : java.util.List[org.daum.common.genmodel.Personne] = {
    personnes
  }
  def getPersonnesForJ : java.util.List[org.daum.common.genmodel.Personne] = {
    personnes
  }

  def setPersonnes(personnes : java.util.List[org.daum.common.genmodel.Personne] ) {
    if(this.personnes!= personnes){
      this.personnes.clear()
      this.personnes.addAll(personnes)
    }

  }

  def addPersonnes(personnes : org.daum.common.genmodel.Personne) {
    this.personnes.add(personnes)
  }

  def addAllPersonnes(personnes : java.util.ArrayList[org.daum.common.genmodel.Personne]) {
    personnes.addAll(personnes)
  }

  def removePersonne(personne : org.daum.common.genmodel.Personne) {
    if(this.personnes.size != 0 ) {
      this.personnes.remove(personne)
    }
  }

  def removeAllPersonnes() {
    this.personnes.clear()
  }

  def getTypeActions : java.util.List[org.daum.common.genmodel.ActionType] = {
    typeActions
  }
  def getTypeActionsForJ : java.util.List[org.daum.common.genmodel.ActionType] = {
    typeActions
  }

  def setTypeActions(typeActions : java.util.ArrayList[org.daum.common.genmodel.ActionType] ) {
    if(this.typeActions!= typeActions){
      this.typeActions.clear()
      this.typeActions.addAll(typeActions)
    }

  }

  def addTypeActions(typeActions : org.daum.common.genmodel.ActionType) {
    this.typeActions.add(typeActions)
  }

  def addAllTypeActions(typeActions : java.util.List[org.daum.common.genmodel.ActionType]) {
    typeActions.addAll(typeActions)
  }

  def removeTypeActions(typeActions : org.daum.common.genmodel.ActionType) {
    if(this.typeActions.size != 0 && this.typeActions.indexOf(typeActions) != -1 ) {
      this.typeActions.remove(this.typeActions.indexOf(typeActions))
    }
  }

  def removeAllTypeActions() {
    this.typeActions.clear();
  }


}
