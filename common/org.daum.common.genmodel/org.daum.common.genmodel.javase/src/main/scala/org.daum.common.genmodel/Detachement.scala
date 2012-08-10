package org.daum.common.genmodel;

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany
import org.daum.library.ormH.annotations.ManyToOne

trait Detachement extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var id : String = ""

  @OneToMany
  private lazy val affectation : java.util.ArrayList[org.daum.common.genmodel.Affectation] = new java.util.ArrayList[org.daum.common.genmodel.Affectation]()

  @ManyToOne
  private var chef : org.daum.common.genmodel.Agent = null


  def getId() : java.lang.String = {
    id
  }

  def getAffectationForJ : java.util.List[org.daum.common.genmodel.Affectation] = {
    affectation
  }

  def setAffectation(affectation : java.util.List[org.daum.common.genmodel.Affectation] ) {
    if(this.affectation!= affectation){
      this.affectation.clear()
      this.affectation.addAll(affectation)
    }

     }

  def addAffectation(affectation : org.daum.common.genmodel.Affectation) {
    this.affectation.add(affectation)
  }

  def addAllAffectation(affectation : java.util.List[org.daum.common.genmodel.Affectation]) {
    affectation.addAll(affectation)
  }

  def removeAffectation(affectation : org.daum.common.genmodel.Affectation) {
    if(this.affectation.size != 0 && this.affectation.indexOf(affectation) != -1 ) {
      this.affectation.remove(this.affectation.indexOf(affectation))
    }
  }

  def removeAllAffectation() {
    this.affectation.clear()
  }

  def getChef : org.daum.common.genmodel.Agent = {
    chef
  }

  def setChef(chef : org.daum.common.genmodel.Agent ) {
    this.chef = (chef)

  }


}
