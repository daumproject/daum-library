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
import org.daum.library.ormH.annotations.OneToMany

trait ActionType extends org.daum.common.genmodel.SitacContainer {
  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var code : java.lang.String = ""


  def getCode : java.lang.String = {
    code
  }

  def setCode(code : java.lang.String) {
    this.code = code
  }
}
