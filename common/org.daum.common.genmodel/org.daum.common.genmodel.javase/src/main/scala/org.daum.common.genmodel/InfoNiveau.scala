package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoNiveau extends org.daum.common.genmodel.SitacContainer {
		private var positionZ : java.lang.String = ""


		def getPositionZ : java.lang.String = {
			positionZ
		}

		def setPositionZ(positionZ : java.lang.String) {
			this.positionZ = positionZ
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoNiveau
		selfObjectClone.setPositionZ(this.getPositionZ)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoNiveau = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.InfoNiveau]
		clonedSelfObject
	}

}
