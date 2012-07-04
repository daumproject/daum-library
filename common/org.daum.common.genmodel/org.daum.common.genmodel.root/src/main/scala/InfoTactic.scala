package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait InfoTactic extends org.sitac.SitacContainer {
		private var categorie : Option[org.sitac.Categorie] = None


		def getCategorie : Option[org.sitac.Categorie] = {
				categorie
		}

		def setCategorie(categorie : Option[org.sitac.Categorie] ) {
				this.categorie = (categorie)

		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoTactic
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoTactic = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoTactic]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.sitac.Categorie]))
		}

		clonedSelfObject
	}

}
