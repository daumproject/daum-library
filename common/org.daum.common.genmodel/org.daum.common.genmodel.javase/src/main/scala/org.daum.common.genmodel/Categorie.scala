package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Categorie extends org.daum.common.genmodel.SitacContainer {
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createCategorie
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Categorie = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Categorie]
		clonedSelfObject
	}

}
