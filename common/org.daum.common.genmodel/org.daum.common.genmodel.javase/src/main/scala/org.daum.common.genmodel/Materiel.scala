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

  private var posRef : org.daum.common.genmodel.Position = null

  private var posTarget : org.daum.common.genmodel.Position = null

  def getPosRef : org.daum.common.genmodel.Position = {
    posRef
  }

  def getPosTarget : org.daum.common.genmodel.Position = {
    posTarget
  }



}