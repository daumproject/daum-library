package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait Action extends org.sitac.SitacContainer with org.sitac.InfoLignePos with org.sitac.InfoHorodate with org.sitac.InfoNiveau {
		private var noria : java.lang.Boolean = false

		private var enLigne : java.lang.Boolean = false

		private var `type` : Option[org.sitac.ActionType] = None


		def getNoria : java.lang.Boolean = {
			noria
		}

		def setNoria(noria : java.lang.Boolean) {
			this.noria = noria
		}

		def getEnLigne : java.lang.Boolean = {
			enLigne
		}

		def setEnLigne(enLigne : java.lang.Boolean) {
			this.enLigne = enLigne
		}

		def getType : Option[org.sitac.ActionType] = {
				`type`
		}

		def setType(`type` : Option[org.sitac.ActionType] ) {
				this.`type` = (`type`)

		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createAction
		selfObjectClone.setHorodatageDebut(this.getHorodatageDebut)
		selfObjectClone.setHorodatageFin(this.getHorodatageFin)
		selfObjectClone.setPositionZ(this.getPositionZ)
		selfObjectClone.setNoria(this.getNoria)
		selfObjectClone.setEnLigne(this.getEnLigne)
		subResult.put(this,selfObjectClone)
		this.getPositions.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Action = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Action]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		this.getPositions.foreach{sub =>
			clonedSelfObject.addPositions(addrs.get(sub).asInstanceOf[org.sitac.Position])
		}

		this.getType.map{sub =>
			clonedSelfObject.setType(Some(addrs.get(sub).asInstanceOf[org.sitac.ActionType]))
		}

		this.getPositions.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
