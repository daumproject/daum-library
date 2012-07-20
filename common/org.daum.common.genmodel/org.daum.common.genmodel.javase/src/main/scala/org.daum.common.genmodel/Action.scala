package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Action extends org.daum.common.genmodel.SitacContainer with IModel with InfoLignePos with InfoHorodate with InfoNiveau {

  private var noria : java.lang.Boolean = false
  private var enLigne : java.lang.Boolean = false
  private var `type` : Option[org.daum.common.genmodel.ActionType] = null
  private var points : java.util.List[GpsPoint] = null

  def getNoria : java.lang.Boolean = {
    noria
  }

  def setNoria(noria : java.lang.Boolean) {
    this.noria = noria
  }

  def getEnLigne : java.lang.Boolean = {
    enLigne
  }

  def setEnLigne(enLigne : java.lang.Boolean) {
    this.enLigne = enLigne
  }

  def getType : Option[org.daum.common.genmodel.ActionType] = {
    `type`
  }

  def setType(`type` : Option[org.daum.common.genmodel.ActionType] ) {
    this.`type` = (`type`)

  }

  def getPoints : java.util.List[GpsPoint] ={
    this.points
  }

  def setPoints(pts : java.util.List[GpsPoint]) {
    this.points = pts
  }

  override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createAction
    selfObjectClone.setHorodatageDebut(this.getHorodatageDebut)
    selfObjectClone.setHorodatageFin(this.getHorodatageFin)
    selfObjectClone.setPositionZ(this.getPositionZ)
    selfObjectClone.setNoria(this.getNoria)
    selfObjectClone.setEnLigne(this.getEnLigne)
    selfObjectClone.setPoints(this.getPoints)
    subResult.put(this,selfObjectClone)
    this.getPositions.foreach{ sub =>
      sub.getClonelazy(subResult)
    }

  }
  override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Action = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Action]
    this.getCategorie.map{sub =>
      clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
    }

    this.getPositions.foreach{sub =>
      clonedSelfObject.addPositions(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Position])
    }

    // TODO something about this.points ?

    this.getType.map{sub =>
      clonedSelfObject.setType(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.ActionType]))
    }

    this.getPositions.foreach{ sub =>
      sub.resolve(addrs)
    }

    clonedSelfObject
  }

}
