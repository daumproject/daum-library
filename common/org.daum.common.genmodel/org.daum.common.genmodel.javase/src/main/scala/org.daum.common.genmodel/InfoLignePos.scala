package org.daum.common.genmodel;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait InfoLignePos extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoTactic {
		private lazy val positions : scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Position] = new scala.collection.mutable.ListBuffer[org.daum.common.genmodel.Position]()


		def getPositions : List[org.daum.common.genmodel.Position] = {
				positions.toList
		}
		def getPositionsForJ : java.util.List[org.daum.common.genmodel.Position] = {
				import scala.collection.JavaConversions._
				positions
		}

		def setPositions(positions : List[org.daum.common.genmodel.Position] ) {
if(this.positions!= positions){
				this.positions.clear()
				this.positions.insertAll(0,positions)
				positions.foreach{el=>
					el.setEContainer(this,Some(()=>{this.removePositions(el)}))
				}
}

		}

		def addPositions(positions : org.daum.common.genmodel.Position) {
				positions.setEContainer(this,Some(()=>{this.removePositions(positions)}))
				this.positions.append(positions)
		}

		def addAllPositions(positions : List[org.daum.common.genmodel.Position]) {
				positions.foreach{ elem => addPositions(elem)}
		}

		def removePositions(positions : org.daum.common.genmodel.Position) {
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
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.daum.common.genmodel.InfoLignePos]
		this.getCategorie.map{sub =>
			clonedSelfObject.setCategorie(Some(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Categorie]))
		}

		this.getPositions.foreach{sub =>
			clonedSelfObject.addPositions(addrs.get(sub).asInstanceOf[org.daum.common.genmodel.Position])
		}

		this.getPositions.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
