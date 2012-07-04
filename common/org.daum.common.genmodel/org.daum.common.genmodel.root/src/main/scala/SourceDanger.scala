package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait SourceDanger extends org.sitac.SitacContainer with org.sitac.InfoPos {
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createSourceDanger
		subResult.put(this,selfObjectClone)
		this.getPositions.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SourceDanger = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.SourceDanger]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPositions(addrs.get(this.getPositions).asInstanceOf[org.sitac.Position])

		this.getPositions.resolve(addrs)

		clonedSelfObject
	}

}
