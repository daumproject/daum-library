package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait Historique extends org.daum.common.genmodel.SitacContainer {
		private lazy val entrees : scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Entree] = new scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Entree]()


		def getEntrees : List[org.daum.common.genmodel.Entree] = {
				entrees.toList
		}
		def getEntreesForJ : java.util.List[org.daum.common.genmodel.Entree] = {
				import scala.collection.JavaConversions._
				entrees
		}

		def setEntrees(entrees : List[org.daum.common.genmodel.Entree] ) {
if(this.entrees!= entrees){
				this.entrees.clear()
				this.entrees.insertAll(0,entrees)
				entrees.foreach{el=>
					el.setEContainer(this,Some(()=>{this.removeEntrees(el)}))
				}
}

		}

		def addEntrees(entrees : org.daum.common.genmodel.Entree) {
				entrees.setEContainer(this,Some(()=>{this.removeEntrees(entrees)}))
				this.entrees.append(entrees)
		}

		def addAllEntrees(entrees : List[org.daum.common.genmodel.Entree]) {
				entrees.foreach{ elem => addEntrees(elem)}
		}

		def removeEntrees(entrees : org.daum.common.genmodel.Entree) {
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
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.Historique]
		this.getEntrees.foreach{sub =>
			clonedSelfObject.addEntrees(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Entree])
		}

		this.getEntrees.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
