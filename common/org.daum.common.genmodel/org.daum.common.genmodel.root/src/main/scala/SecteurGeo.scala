package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SecteurGeo extends org.sitac.SitacContainer with org.sitac.InfoZone with org.sitac.InfoResponsable {
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createSecteurGeo
		selfObjectClone.setNom(this.getNom)
		selfObjectClone.setNiveau(this.getNiveau)
		subResult.put(this,selfObjectClone)
		this.getPositions.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : SecteurGeo = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.SecteurGeo]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		this.getPositions.foreach{sub =>
			clonedSelfObject.addPositions(addrs.get(sub).asInstanceOf[org.sitac.Position])
		}

		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.sitac.Moyen])

		this.getPositions.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
