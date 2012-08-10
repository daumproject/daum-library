package org.daum.common.genmodel;


trait InfoResponsable extends org.daum.common.genmodel.SitacContainer {
		private var niveau : java.lang.String = ""

		private var chef : org.daum.common.genmodel.Moyens = _


		def getNiveau : java.lang.String = {
			niveau
		}

		def setNiveau(niveau : java.lang.String) {
			this.niveau = niveau
		}

		def getChef : org.daum.common.genmodel.Moyens = {
				chef
		}

		def setChef(chef : org.daum.common.genmodel.Moyens ) {
				this.chef = (chef)

		}
}
