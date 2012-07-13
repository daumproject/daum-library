package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoLignePos extends org.sitac.SitacContainer with org.sitac.InfoTactic {
		private lazy val positions : scala.collection.mutable.ListBuffer[org.sitac.Position] = new scala.collection.mutable.ListBuffer[org.sitac.Position]()


		def getPositions : List[org.sitac.Position] = {
				positions.toList
		}
		def getPositionsForJ : java.util.List[org.sitac.Position] = {
				import scala.collection.JavaConversions._
				positions
		}

		def setPositions(positions : List[org.sitac.Position] ) {
if(this.positions!= positions){
				this.positions.clear()
				this.positions.insertAll(0,positions)
				positions.foreach{el=>
					el.setEContainer(this,Some(()=>{this.removePositions(el)}))
				}
}

		}

		def addPositions(positions : org.sitac.Position) {
				positions.setEContainer(this,Some(()=>{this.removePositions(positions)}))
				this.positions.append(positions)
		}

		def addAllPositions(positions : List[org.sitac.Position]) {
				positions.foreach{ elem => addPositions(elem)}
		}

		def removePositions(positions : org.sitac.Position) {
				if(this.positions.size != 0 && this.positions.indexOf(positions) != -1 ) {
						this.positions.remove(this.positions.indexOf(positions))
						positions.setEContainer(null,None)
				}
		}

		def removeAllPositions() {
				this.positions.foreach{ elem => removePositions(elem)}
		}
	override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createInfoLignePos
		subResult.put(this,selfObjectClone)
		this.getPositions.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

	}
	override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : InfoLignePos = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.InfoLignePos]
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
