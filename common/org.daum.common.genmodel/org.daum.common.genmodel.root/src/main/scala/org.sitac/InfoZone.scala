package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoZone extends org.sitac.SitacContainer with org.sitac.InfoLignePos {
		private var nom : java.lang.String = ""


		def getNom : java.lang.String = {
			nom
		}

		def setNom(nom : java.lang.String) {
			this.nom = nom
		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoZone
		selfObjectClone.setNom(this.getNom)
		subResult.put(this,selfObjectClone)
		this.getPositions.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoZone = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoZone]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		this.getPositions.foreach{sub =>
			clonedSelfObject.addPositions(addrs.get(sub).asInstanceOf[org.sitac.Position])
		}

		this.getPositions.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
