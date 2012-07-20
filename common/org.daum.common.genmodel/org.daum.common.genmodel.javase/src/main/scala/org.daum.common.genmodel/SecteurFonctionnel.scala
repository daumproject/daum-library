package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SecteurFonctionnel extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoPos with org.daum.common.genmodel.InfoResponsable {

	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createSecteurFonctionnel
		selfObjectClone.setNiveau(this.getNiveau)
		subResult.put(this,selfObjectClone)
		this.getPosition.getClonelazy(subResult)

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SecteurFonctionnel = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.SecteurFonctionnel]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		clonedSelfObject.setPosition(addrs.get(this.getPosition).asInstanceOf[org.daum.common.genmodel.Position])

		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.daum.common.genmodel.Moyens])

		this.getPosition.resolve(addrs)

		clonedSelfObject
	}

}
