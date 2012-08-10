package org.daum.common.genmodel;


trait PriseEau extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoPos {
		private var perenne : java.lang.Boolean = false


		def getPerenne : java.lang.Boolean = {
			perenne
		}

		def setPerenne(perenne : java.lang.Boolean) {
			this.perenne = perenne
		}

}
