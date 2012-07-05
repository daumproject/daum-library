package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany
import org.daum.library.ormH.annotations.ManyToOne

trait Detachement extends org.sitac.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var id : String = ""

  @OneToMany
  private lazy val affectation : scala.collection.mutable.ListBuffer[org.sitac.Affectation] = new scala.collection.mutable.ListBuffer[org.sitac.Affectation]()

  @ManyToOne
  private var chef : Option[org.sitac.Agent] = None


  def getId() : java.lang.String = {
    id
  }

  def getAffectation : List[org.sitac.Affectation] = {
    affectation.toList
  }
  def getAffectationForJ : java.util.List[org.sitac.Affectation] = {
    import scala.collection.JavaConversions._
    affectation
  }

  def setAffectation(affectation : List[org.sitac.Affectation] ) {
    if(this.affectation!= affectation){
      this.affectation.clear()
      this.affectation.insertAll(0,affectation)
      affectation.foreach{el=>
        el.setEContainer(this,Some(()=>{this.removeAffectation(el)}))
      }
    }

  }

  def addAffectation(affectation : org.sitac.Affectation) {
    affectation.setEContainer(this,Some(()=>{this.removeAffectation(affectation)}))
    this.affectation.append(affectation)
  }

  def addAllAffectation(affectation : List[org.sitac.Affectation]) {
    affectation.foreach{ elem => addAffectation(elem)}
  }

  def removeAffectation(affectation : org.sitac.Affectation) {
    if(this.affectation.size != 0 && this.affectation.indexOf(affectation) != -1 ) {
      this.affectation.remove(this.affectation.indexOf(affectation))
      affectation.setEContainer(null,None)
    }
  }

  def removeAllAffectation() {
    this.affectation.foreach{ elem => removeAffectation(elem)}
  }

  def getChef : Option[org.sitac.Agent] = {
    chef
  }

  def setChef(chef : Option[org.sitac.Agent] ) {
    this.chef = (chef)

  }
  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createDetachement
    subResult.put(this,selfObjectClone)
    this.getAffectation.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

  }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Detachement = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Detachement]
    this.getAffectation.foreach{sub =>
      clonedSelfObject.addAffectation(addrs.get(sub).asInstanceOf[org.sitac.Affectation])
    }

    this.getChef.map{sub =>
      clonedSelfObject.setChef(Some(addrs.get(sub).asInstanceOf[org.sitac.Agent]))
    }

    this.getAffectation.foreach{ sub =>
      sub.resolve(addrs)
    }

    clonedSelfObject
  }

}
