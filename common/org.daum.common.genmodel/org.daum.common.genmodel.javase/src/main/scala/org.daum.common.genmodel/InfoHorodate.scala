package org.daum.common.genmodel;


trait InfoHorodate extends org.daum.common.genmodel.SitacContainer {
		private var horodatageDebut : java.util.Date=_
		private var horodatageFin : java.util.Date=_

		def getHorodatageDebut : java.util.Date = {
			horodatageDebut
		}

		def setHorodatageDebut(horodatageDebut : java.util.Date) {
			this.horodatageDebut = horodatageDebut
		}

		def getHorodatageFin : java.util.Date = {
			horodatageFin
		}

		def setHorodatageFin(horodatageFin : java.util.Date) {
			this.horodatageFin = horodatageFin
		}

}
