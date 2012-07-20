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

trait Personne extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  var id : java.lang.String = ""

  private var nom : java.lang.String = ""
  private var prenom : java.lang.String = ""


  private var posRef : Option[org.daum.common.genmodel.Position] = null

  private var posTarget : Option[org.daum.common.genmodel.Position] = null

  def getPosRef : Option[org.daum.common.genmodel.Position] = {
    posRef
  }

  def getPosTarget : Option[org.daum.common.genmodel.Position] = {
    posTarget
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


  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createPersonne
    selfObjectClone.setNom(this.getNom)
    selfObjectClone.setPrenom(this.getPrenom)
    subResult.put(this,selfObjectClone)
  }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Personne = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Personne]
    clonedSelfObject
  }

}
