package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SecteurFontionel extends org.sitac.SitacContainer with org.sitac.InfoPos with org.sitac.InfoResponsable {
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createSecteurFontionel
		selfObjectClone.setNiveau(this.getNiveau)
		subResult.put(this,selfObjectClone)
		this.getPositions.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SecteurFontionel = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.SecteurFontionel]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPositions(addrs.get(this.getPositions).asInstanceOf[org.sitac.Position])

		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.sitac.Moyen])

		this.getPositions.resolve(addrs)

		clonedSelfObject
	}

}
