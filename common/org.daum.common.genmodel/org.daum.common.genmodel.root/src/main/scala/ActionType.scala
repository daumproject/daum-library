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

trait ActionType extends org.sitac.SitacContainer {
  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var code : java.lang.String = ""


  def getCode : java.lang.String = {
    code
  }

  def setCode(code : java.lang.String) {
    this.code = code
  }
  def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    val selfObjectClone = SitacFactory.createActionType
    selfObjectClone.setCode(this.getCode)
    subResult.put(this,selfObjectClone)
  }
  def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : ActionType = {
    val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.ActionType]
    clonedSelfObject
  }

}
