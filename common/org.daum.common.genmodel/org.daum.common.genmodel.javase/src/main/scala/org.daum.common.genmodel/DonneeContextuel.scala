package org.daum.common.genmodel;


trait DonneeContextuel extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoPos {
		private var orientation : java.lang.Integer = 0

		private var precision : java.lang.String = ""


		def getOrientation : java.lang.Integer = {
			orientation
		}

		def setOrientation(orientation : java.lang.Integer) {
			this.orientation = orientation
		}

		def getPrecision : java.lang.String = {
			precision
		}

		def setPrecision(precision : java.lang.String) {
			this.precision = precision
		}


}
