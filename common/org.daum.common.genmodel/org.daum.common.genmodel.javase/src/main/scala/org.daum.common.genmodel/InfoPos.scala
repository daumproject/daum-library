package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoPos extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoTactic {
		private var position : org.daum.common.genmodel.Position = _


		def getPosition : org.daum.common.genmodel.Position = {
				position
		}

		def setPosition(position : org.daum.common.genmodel.Position ) {
      if(this.position!= position) {
				this.position = (position)
				position.setEContainer(this, Some(() => { this.position= _:org.daum.common.genmodel.Position }) )
}

		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoPos
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoPos = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.InfoPos]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.daum.common.genmodel.Position])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
