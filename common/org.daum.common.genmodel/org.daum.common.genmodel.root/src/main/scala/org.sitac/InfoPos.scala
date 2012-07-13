package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoPos extends org.sitac.SitacContainer with org.sitac.InfoTactic {
		private var position : org.sitac.Position = _


		def getPosition : org.sitac.Position = {
				position
		}

		def setPosition(position : org.sitac.Position ) {
      if(this.position!= position) {
				this.position = (position)
				position.setEContainer(this, Some(() => { this.position= _:org.sitac.Position }) )
}

		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoPos
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoPos = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoPos]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.sitac.Position])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
