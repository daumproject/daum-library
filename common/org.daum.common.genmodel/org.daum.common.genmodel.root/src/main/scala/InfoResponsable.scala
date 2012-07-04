package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait InfoResponsable extends org.sitac.SitacContainer {
		private var niveau : java.lang.String = ""

		private var chef : org.sitac.Moyen = _


		def getNiveau : java.lang.String = {
			niveau
		}

		def setNiveau(niveau : java.lang.String) {
			this.niveau = niveau
		}

		def getChef : org.sitac.Moyen = {
				chef
		}

		def setChef(chef : org.sitac.Moyen ) {
				this.chef = (chef)

		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoResponsable
		selfObjectClone.setNiveau(this.getNiveau)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoResponsable = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoResponsable]
		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.sitac.Moyen])

		clonedSelfObject
	}

}
