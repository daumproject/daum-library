package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait PriseEau extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoPos {
		private var perenne : java.lang.Boolean = false


		def getPerenne : java.lang.Boolean = {
			perenne
		}

		def setPerenne(perenne : java.lang.Boolean) {
			this.perenne = perenne
		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createPriseEau
		selfObjectClone.setPerenne(this.getPerenne)
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : PriseEau = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.PriseEau]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.daum.common.genmodel.Position])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
