package org.daum.common.genmodel;
/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/07/12
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */


import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType

trait Materiel extends org.daum.common.genmodel.SitacContainer {
  @Id
  @Generated(strategy = GeneratedType.UUID)
  var id : java.lang.String = ""

  def getId() : java.lang.String = {
    id
  }

  private var posRef : Option[org.daum.common.genmodel.Position] = null

  private var posTarget : Option[org.daum.common.genmodel.Position] = null

  def getPosRef : Option[org.daum.common.genmodel.Position] = {
    posRef
  }

  def getPosTarget : Option[org.daum.common.genmodel.Position] = {
    posTarget
  }



}