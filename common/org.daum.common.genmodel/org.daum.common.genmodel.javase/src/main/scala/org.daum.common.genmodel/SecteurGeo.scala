package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SecteurGeo extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoZone with org.daum.common.genmodel.InfoResponsable {
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
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.SecteurGeo]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		this.getPositions.foreach{sub =>
			clonedSelfObject.addPositions(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Position])
		}

		clonedSelfObject.setChef(addrs.get(this.getChef).asInstanceOf[org.daum.common.genmodel.Moyens])

		this.getPositions.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
