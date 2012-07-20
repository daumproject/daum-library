package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait DonneeContextuel extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoPos {
		private var orientation : java.lang.Integer = 0

		private var precision : java.lang.String = ""


		def getOrientation : java.lang.Integer = {
			orientation
		}

		def setOrientation(orientation : java.lang.Integer) {
			this.orientation = orientation
		}

		def getPrecision : java.lang.String = {
			precision
		}

		def setPrecision(precision : java.lang.String) {
			this.precision = precision
		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createDonneeContextuel
		selfObjectClone.setOrientation(this.getOrientation)
		selfObjectClone.setPrecision(this.getPrecision)
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : DonneeContextuel = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.DonneeContextuel]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.daum.common.genmodel.Position])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
