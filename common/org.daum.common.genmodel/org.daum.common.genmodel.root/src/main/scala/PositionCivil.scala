package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait PositionCivil extends org.sitac.SitacContainer with org.sitac.Position {
		private var nomRue : java.lang.String = ""

		private var cp : java.lang.String = ""

		private var numeroRue : java.lang.String = ""

		private var pays : java.lang.String = ""


		def getNomRue : java.lang.String = {
			nomRue
		}

		def setNomRue(nomRue : java.lang.String) {
			this.nomRue = nomRue
		}

		def getCp : java.lang.String = {
			cp
		}

		def setCp(cp : java.lang.String) {
			this.cp = cp
		}

		def getNumeroRue : java.lang.String = {
			numeroRue
		}

		def setNumeroRue(numeroRue : java.lang.String) {
			this.numeroRue = numeroRue
		}

		def getPays : java.lang.String = {
			pays
		}

		def setPays(pays : java.lang.String) {
			this.pays = pays
		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createPositionCivil
		selfObjectClone.setNomRue(this.getNomRue)
		selfObjectClone.setCp(this.getCp)
		selfObjectClone.setNumeroRue(this.getNumeroRue)
		selfObjectClone.setPays(this.getPays)
		subResult.put(this,selfObjectClone)
	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : PositionCivil = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.PositionCivil]
		clonedSelfObject
	}

}
