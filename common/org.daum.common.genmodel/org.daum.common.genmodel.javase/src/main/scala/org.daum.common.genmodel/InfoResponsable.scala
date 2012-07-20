package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoResponsable extends org.daum.common.genmodel.SitacContainer {
		private var niveau : java.lang.String = ""

		private var chef : org.daum.common.genmodel.Moyens = _


		def getNiveau : java.lang.String = {
			niveau
		}

		def setNiveau(niveau : java.lang.String) {
			this.niveau = niveau
		}

		def getChef : org.daum.common.genmodel.Moyens = {
				chef
		}

		def setChef(chef : org.daum.common.genmodel.Moyens ) {
				this.chef = (chef)

		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoResponsable
		selfObjectClone.setNiveau(this.getNiveau)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoResponsable = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.InfoResponsable]
		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.daum.common.genmodel.Moyens])

		clonedSelfObject
	}

}
