package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait MessageAmbiance extends org.sitac.SitacContainer {
		private var jeSuis : java.lang.String = ""

		private var jeVois : java.lang.String = ""

		private var id : java.lang.String = ""


		def getJeSuis : java.lang.String = {
			jeSuis
		}

		def setJeSuis(jeSuis : java.lang.String) {
			this.jeSuis = jeSuis
		}

		def getJeVois : java.lang.String = {
			jeVois
		}

		def setJeVois(jeVois : java.lang.String) {
			this.jeVois = jeVois
		}

		def getId : java.lang.String = {
			id
		}

		def setId(id : java.lang.String) {
			this.id = id
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createMessageAmbiance
		selfObjectClone.setJeSuis(this.getJeSuis)
		selfObjectClone.setJeVois(this.getJeVois)
		selfObjectClone.setId(this.getId)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : MessageAmbiance = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.MessageAmbiance]
		clonedSelfObject
	}

}
