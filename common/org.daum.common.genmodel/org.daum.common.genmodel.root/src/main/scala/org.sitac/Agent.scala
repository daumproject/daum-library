package org.sitac

import java.io.Serializable;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Agent extends org.sitac.SitacContainer with org.sitac.Personne with Serializable
{

  private var matricule : java.lang.String = ""

  private var password : java.lang.String = ""

  private lazy val capteurs : java.util.HashMap[String,org.sitac.Capteurs] = new java.util.HashMap[String,org.sitac.Capteurs]()
    // todo
  private  var auth : org.sitac.AutorisationType  =  AutorisationType.READONLY

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

  def setAutorisation(auth : org.sitac.AutorisationType){
    this.auth = auth
  }

  def getAutorisation : org.sitac.AutorisationType = {
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
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Agent]
    clonedSelfObject
  }


  def getCapteurs :java.util.HashMap[String,org.sitac.Capteurs] = {
    capteurs
  }




}
