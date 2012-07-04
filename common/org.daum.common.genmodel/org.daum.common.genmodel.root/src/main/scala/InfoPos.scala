package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait InfoPos extends org.sitac.SitacContainer with org.sitac.InfoTactic {
		private var positions : org.sitac.Position = _


		def getPositions : org.sitac.Position = {
				positions
		}

		def setPositions(positions : org.sitac.Position ) {
if(this.positions!= positions){
				this.positions = (positions)
				positions.setEContainer(this, Some(() => { this.positions= _:org.sitac.Position }) )
}

		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoPos
		subResult.put(this,selfObjectClone)
		this.getPositions.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoPos = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoPos]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPositions(addrs.get(this.getPositions).asInstanceOf[org.sitac.Position])

		this.getPositions.resolve(addrs)

		clonedSelfObject
	}

}
