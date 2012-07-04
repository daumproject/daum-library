package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait Historique extends org.sitac.SitacContainer {
		private lazy val entrees : scala.collection.mutable.ListBuffer[org.sitac.Entree] = new scala.collection.mutable.ListBuffer[org.sitac.Entree]()


		def getEntrees : List[org.sitac.Entree] = {
				entrees.toList
		}
		def getEntreesForJ : java.util.List[org.sitac.Entree] = {
				import scala.collection.JavaConversions._
				entrees
		}

		def setEntrees(entrees : List[org.sitac.Entree] ) {
if(this.entrees!= entrees){
				this.entrees.clear()
				this.entrees.insertAll(0,entrees)
				entrees.foreach{el=>
					el.setEContainer(this,Some(()=>{this.removeEntrees(el)}))
				}
}

		}

		def addEntrees(entrees : org.sitac.Entree) {
				entrees.setEContainer(this,Some(()=>{this.removeEntrees(entrees)}))
				this.entrees.append(entrees)
		}

		def addAllEntrees(entrees : List[org.sitac.Entree]) {
				entrees.foreach{ elem => addEntrees(elem)}
		}

		def removeEntrees(entrees : org.sitac.Entree) {
				if(this.entrees.size != 0 && this.entrees.indexOf(entrees) != -1 ) {
						this.entrees.remove(this.entrees.indexOf(entrees))
						entrees.setEContainer(null,None)
				}
		}

		def removeAllEntrees() {
				this.entrees.foreach{ elem => removeEntrees(elem)}
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createHistorique
		subResult.put(this,selfObjectClone)
		this.getEntrees.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Historique = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Historique]
		this.getEntrees.foreach{sub =>
			clonedSelfObject.addEntrees(addrs.get(sub).asInstanceOf[org.sitac.Entree])
		}

		this.getEntrees.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
