package org.sitac


/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Cible extends org.sitac.SitacContainer with org.sitac.IModel with org.sitac.InfoPos {

  private var cibleType : CibleType = CibleType.WATER

  def getType : CibleType ={
    this.cibleType
  }

  def setType(cibleType : CibleType) {
    this.cibleType = cibleType
  }

	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createCible
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}

	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Cible = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Cible]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.sitac.Position])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
