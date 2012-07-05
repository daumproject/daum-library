package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait MoyenType extends org.sitac.SitacContainer {
		private var code : java.lang.Integer = 0


		def getCode : java.lang.Integer = {
			code
		}

		def setCode(code : java.lang.Integer) {
			this.code = code
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createMoyenType
		selfObjectClone.setCode(this.getCode)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : MoyenType = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.MoyenType]
		clonedSelfObject
	}

}
