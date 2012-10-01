package org.daum.common.genmodel;

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType

trait Personne extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  var id : java.lang.String = ""

  private var nom : java.lang.String = ""
  private var prenom : java.lang.String = ""


  private var posRef : org.daum.common.genmodel.Position = null

  private var posTarget : org.daum.common.genmodel.Position = null

  def getPosRef : org.daum.common.genmodel.Position = {
    posRef
  }

  def getPosTarget : org.daum.common.genmodel.Position = {
    posTarget
  }

  def setposRef(posRef : org.daum.common.genmodel.Position) {
    this.posRef = posRef
  }

  def setposTarget(posTarget : org.daum.common.genmodel.Position) {
    this.posTarget = posTarget
  }

  def getNom : java.lang.String = {
    nom
  }

  def setNom(nom : java.lang.String) {
    this.nom = nom
  }

  def getPrenom : java.lang.String = {
    prenom
  }

  def setPrenom(prenom : java.lang.String) {
    this.prenom = prenom
  }


  def getId() : java.lang.String = {
    id
  }


}
