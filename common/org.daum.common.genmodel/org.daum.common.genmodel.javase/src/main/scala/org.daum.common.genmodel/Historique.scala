package org.daum.common.genmodel;


trait Historique extends org.daum.common.genmodel.SitacContainer {
		private lazy val entrees : java.util.ArrayList[org.daum.common.genmodel.Entree] = new java.util.ArrayList[org.daum.common.genmodel.Entree]()


		def getEntrees : java.util.List[org.daum.common.genmodel.Entree] = {
				entrees
		}
		def getEntreesForJ : java.util.List[org.daum.common.genmodel.Entree] = {
				import scala.collection.JavaConversions._
				entrees
		}

		def setEntrees(entrees : java.util.List[org.daum.common.genmodel.Entree] ) {
if(this.entrees!= entrees){
				this.entrees.clear()
				this.entrees.addAll(entrees)

		}
    }

		def addEntrees(entrees : org.daum.common.genmodel.Entree) {
				this.entrees.add(entrees)
		}

		def addAllEntrees(entrees : java.util.List[org.daum.common.genmodel.Entree]) {
				entrees.addAll(entrees)
		}

		def removeEntrees(entrees : org.daum.common.genmodel.Entree) {
				if(this.entrees.size != 0 && this.entrees.indexOf(entrees) != -1 ) {
						this.entrees.remove(this.entrees.indexOf(entrees))
				}
		}

		def removeAllEntrees() {
				this.entrees.clear()
		}

}
