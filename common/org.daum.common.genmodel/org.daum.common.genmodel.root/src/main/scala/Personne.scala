package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait Personne extends org.sitac.SitacContainer {
		private var nom : java.lang.String = ""

		private var prenom : java.lang.String = ""


		def getNom : java.lang.String = {
			nom
		}

		def setNom(nom : java.lang.String) {
			this.nom = nom
		}

		def getPrenom : java.lang.String = {
			prenom
		}

		def setPrenom(prenom : java.lang.String) {
			this.prenom = prenom
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createPersonne
		selfObjectClone.setNom(this.getNom)
		selfObjectClone.setPrenom(this.getPrenom)
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Personne = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Personne]
		clonedSelfObject
	}

}
