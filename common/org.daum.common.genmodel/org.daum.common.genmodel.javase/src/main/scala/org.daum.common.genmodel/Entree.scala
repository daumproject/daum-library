package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Entree extends org.daum.common.genmodel.SitacContainer {
		private var horodatage : java.lang.String = ""

		private var `type` : org.daum.common.genmodel.TypeEntree = _


		def getHorodatage : java.lang.String = {
			horodatage
		}

		def setHorodatage(horodatage : java.lang.String) {
			this.horodatage = horodatage
		}

		def getType : org.daum.common.genmodel.TypeEntree = {
				`type`
		}

		def setType(`type` : org.daum.common.genmodel.TypeEntree ) {
				this.`type` = (`type`)

		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createEntree
		selfObjectClone.setHorodatage(this.getHorodatage)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Entree = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Entree]
		clonedSelfObject.setType(addrs.get(this.getType).asInstanceOf[org.daum.common.genmodel.TypeEntree])

		clonedSelfObject
	}

}
