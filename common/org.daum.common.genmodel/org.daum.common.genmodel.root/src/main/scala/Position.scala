package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Position extends org.sitac.SitacContainer {
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createPosition
		subResult.put(this,selfObjectClone)
	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Position = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Position]
		clonedSelfObject
	}

}
