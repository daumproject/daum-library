package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait TypeEntree extends org.sitac.SitacContainer {
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createTypeEntree
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : TypeEntree = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.TypeEntree]
		clonedSelfObject
	}

}
