package org.daum.common.genmodel;

import java.io.Serializable;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */

import org.daum.library.ormH.annotations.Id

trait Agent extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.Personne with Serializable
{
  @Id
  private var matricule : java.lang.String = ""

  private var password : java.lang.String = ""

  private lazy val capteurs : java.util.HashMap[String,org.daum.common.genmodel.Capteurs] = new java.util.HashMap[String,org.daum.common.genmodel.Capteurs]()
  // todo
  private  var auth : org.daum.common.genmodel.AutorisationType  =  AutorisationType.READONLY

  def getPassword : java.lang.String = {
    password
  }

  /**
   * MD5 password
   * @param password
   */
  def setPassword(password : java.lang.String){
    this.password = password
  }

  def getMatricule : java.lang.String = {
    matricule
  }

  def getCapteur(id : java.lang.String) : Capteurs=
  {
    if (capteurs.get(id) == null) {
      capteurs.put(id, SitacFactory.createDatedValue)
    }
    capteurs.get(id)
  }

  def setAutorisation(auth : org.daum.common.genmodel.AutorisationType){
    this.auth = auth
  }

  def getAutorisation : org.daum.common.genmodel.AutorisationType = {
    auth
  }

  def setMatricule(matricule : java.lang.String) {
    this.matricule = matricule
  }
  override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createAgent
    selfObjectClone.setNom(this.getNom)
    selfObjectClone.setPrenom(this.getPrenom)
    selfObjectClone.setMatricule(this.getMatricule)
    subResult.put(this,selfObjectClone)
  }
  override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Agent = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Agent]
    clonedSelfObject
  }


  def getCapteurs :java.util.HashMap[String,org.daum.common.genmodel.Capteurs] = {
    capteurs
  }




}
