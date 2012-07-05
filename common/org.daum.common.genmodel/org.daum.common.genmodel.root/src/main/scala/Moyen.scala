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


trait Moyen extends org.sitac.SitacContainer with org.sitac.Detachement {
  private var precision : java.lang.String = ""

  private var numero : java.lang.String = ""

  private var `type` : Option[org.sitac.MoyenType] = None

  @OneToMany
  private lazy val personnels : scala.collection.mutable.ListBuffer[org.sitac.Agent] = new scala.collection.mutable.ListBuffer[org.sitac.Agent]()

  private var posRef : Option[org.sitac.Position] = None

  private var posTarget : Option[org.sitac.Position] = None


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

  def getType : Option[org.sitac.MoyenType] = {
    `type`
  }

  def setType(`type` : Option[org.sitac.MoyenType] ) {
    this.`type` = (`type`)

  }

  def getPersonnels : List[org.sitac.Agent] = {
    personnels.toList
  }
  def getPersonnelsForJ : java.util.List[org.sitac.Agent] = {
    import scala.collection.JavaConversions._
    personnels
  }

  def setPersonnels(personnels : List[org.sitac.Agent] ) {
    this.personnels.clear()
    this.personnels.insertAll(0,personnels)

  }

  def addPersonnels(personnels : org.sitac.Agent) {
    this.personnels.append(personnels)
  }

  def addAllPersonnels(personnels : List[org.sitac.Agent]) {
    personnels.foreach{ elem => addPersonnels(elem)}
  }

  def removePersonnels(personnels : org.sitac.Agent) {
    if(this.personnels.size != 0 && this.personnels.indexOf(personnels) != -1 ) {
      this.personnels.remove(this.personnels.indexOf(personnels))
    }
  }

  def removeAllPersonnels() {
    this.personnels.foreach{ elem => removePersonnels(elem)}
  }

  def getPosRef : Option[org.sitac.Position] = {
    posRef
  }

  def setPosRef(posRef : Option[org.sitac.Position] ) {
    if(this.posRef!= posRef){
      this.posRef = (posRef)
      posRef.map{ dic=>				dic.setEContainer(this, Some(() => { this.posRef= None }) )
      }}

  }

  def getPosTarget : Option[org.sitac.Position] = {
    posTarget
  }

  def setPosTarget(posTarget : Option[org.sitac.Position] ) {
    if(this.posTarget!= posTarget){
      this.posTarget = (posTarget)
      posTarget.map{ dic=>				dic.setEContainer(this, Some(() => { this.posTarget= None }) )
      }}

  }
  override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createMoyen
    selfObjectClone.setPrecision(this.getPrecision)
    selfObjectClone.setNumero(this.getNumero)
    subResult.put(this,selfObjectClone)
    this.getAffectation.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getPosRef.map{ sub =>
      sub.getClonelazy(subResult)
    }

    this.getPosTarget.map{ sub =>
      sub.getClonelazy(subResult)
    }

  }
  override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Moyen = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Moyen]
    this.getAffectation.foreach{sub =>
      clonedSelfObject.addAffectation(addrs.get(sub).asInstanceOf[org.sitac.Affectation])
    }

    this.getChef.map{sub =>
      clonedSelfObject.setChef(Some(addrs.get(sub).asInstanceOf[org.sitac.Agent]))
    }

    this.getType.map{sub =>
      clonedSelfObject.setType(Some(addrs.get(sub).asInstanceOf[org.sitac.MoyenType]))
    }

    this.getPersonnels.foreach{sub =>
      clonedSelfObject.addPersonnels(addrs.get(sub).asInstanceOf[org.sitac.Agent])
    }

    this.getPosRef.map{sub =>
      clonedSelfObject.setPosRef(Some(addrs.get(sub).asInstanceOf[org.sitac.Position]))
    }

    this.getPosTarget.map{sub =>
      clonedSelfObject.setPosTarget(Some(addrs.get(sub).asInstanceOf[org.sitac.Position]))
    }

    this.getAffectation.foreach{ sub =>
      sub.resolve(addrs)
    }

    this.getPosRef.map{ sub =>
      sub.resolve(addrs)
    }

    this.getPosTarget.map{ sub =>
      sub.resolve(addrs)
    }

    clonedSelfObject
  }

}
