package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait InfoHorodate extends org.sitac.SitacContainer {
		private var horodatageDebut : java.util.Date=_
		private var horodatageFin : java.util.Date=_

		def getHorodatageDebut : java.util.Date = {
			horodatageDebut
		}

		def setHorodatageDebut(horodatageDebut : java.util.Date) {
			this.horodatageDebut = horodatageDebut
		}

		def getHorodatageFin : java.util.Date = {
			horodatageFin
		}

		def setHorodatageFin(horodatageFin : java.util.Date) {
			this.horodatageFin = horodatageFin
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoHorodate
		selfObjectClone.setHorodatageDebut(this.getHorodatageDebut)
		selfObjectClone.setHorodatageFin(this.getHorodatageFin)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoHorodate = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoHorodate]
		clonedSelfObject
	}

}
