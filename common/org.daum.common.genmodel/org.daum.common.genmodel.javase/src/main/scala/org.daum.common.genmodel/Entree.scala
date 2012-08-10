package org.daum.common.genmodel;


trait Entree extends org.daum.common.genmodel.SitacContainer {
		private var horodatage : java.lang.String = ""

		private var `type` : org.daum.common.genmodel.TypeEntree = _


		def getHorodatage : java.lang.String = {
			horodatage
		}

		def setHorodatage(horodatage : java.lang.String) {
			this.horodatage = horodatage
		}

		def getType : org.daum.common.genmodel.TypeEntree = {
				`type`
		}

		def setType(`type` : org.daum.common.genmodel.TypeEntree ) {
				this.`type` = (`type`)

		}
}
