package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SourceDanger extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.IModel with org.daum.common.genmodel.InfoPos {

  private var dangerType : DangerType = DangerType.FIRE

  def getType : DangerType ={
    this.dangerType
  }

  def setType(dangerType : DangerType) {
    this.dangerType = dangerType
  }

	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createSourceDanger
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SourceDanger = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.SourceDanger]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.daum.common.genmodel.Position])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
