package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SecteurFonctionnel extends org.sitac.SitacContainer with org.sitac.InfoPos with org.sitac.InfoResponsable {

	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createSecteurFonctionnel
		selfObjectClone.setNiveau(this.getNiveau)
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SecteurFonctionnel = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.SecteurFonctionnel]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.sitac.Position])

		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.sitac.Moyens])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
