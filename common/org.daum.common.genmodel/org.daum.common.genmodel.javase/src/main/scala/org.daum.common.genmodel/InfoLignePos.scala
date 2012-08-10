package org.daum.common.genmodel;


trait InfoLignePos extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoTactic {
  private lazy val positions : java.util.ArrayList[org.daum.common.genmodel.Position] = new java.util.ArrayList[org.daum.common.genmodel.Position]()

  def getPositions : java.util.List[org.daum.common.genmodel.Position] = {
    positions
  }
  def getPositionsForJ : java.util.List[org.daum.common.genmodel.Position] = {
    positions
  }

  def setPositions(positions : java.util.List[org.daum.common.genmodel.Position] ) {
    if(this.positions!= positions){
      this.positions.clear()
      this.positions.addAll(positions)
    }
  }

  def addPositions(positions : org.daum.common.genmodel.Position) {
    this.positions.add(positions)
  }

  def addAllPositions(positions : java.util.List[org.daum.common.genmodel.Position]) {
    positions.addAll(positions)
  }

  def removePositions(positions : org.daum.common.genmodel.Position) {
    if(this.positions.size != 0 && this.positions.indexOf(positions) != -1 ) {
      this.positions.remove(this.positions.indexOf(positions))
    }
  }

  def removeAllPositions() {
    this.positions.clear()
  }


}
