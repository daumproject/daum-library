package org.daum.common.genmodel;


trait InfoZone extends org.daum.common.genmodel.SitacContainer with org.daum.common.genmodel.InfoLignePos {
		private var nom : java.lang.String = ""
		def getNom : java.lang.String = {
			nom
		}

		def setNom(nom : java.lang.String) {
			this.nom = nom
		}

}
