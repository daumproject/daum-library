package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait DonneeContextuel extends org.sitac.SitacContainer with org.sitac.InfoPos {
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
		this.getPositions.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : DonneeContextuel = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.DonneeContextuel]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPositions(addrs.get(this.getPositions).asInstanceOf[org.sitac.Position])

		this.getPositions.resolve(addrs)

		clonedSelfObject
	}

}
