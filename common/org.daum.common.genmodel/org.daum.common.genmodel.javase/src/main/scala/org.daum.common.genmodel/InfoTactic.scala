package org.daum.common.genmodel;



import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType


trait InfoTactic extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  var id : java.lang.String = ""

  def getId : java.lang.String ={ id }

  private var categorie : org.daum.common.genmodel.Categorie = null

  def getCategorie : org.daum.common.genmodel.Categorie = {
    categorie
  }

  def setCategorie(categorie : org.daum.common.genmodel.Categorie ) {
    this.categorie = (categorie)
  }


}
