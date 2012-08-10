package org.daum.common.genmodel;


import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType
import org.daum.library.ormH.annotations.OneToMany

trait CodeSinistre extends org.daum.common.genmodel.SitacContainer {

  @Id
  @Generated(strategy = GeneratedType.UUID)
  private var code : java.lang.String = ""


		def getCode : java.lang.String = {
			code
		}

		def setCode(code : java.lang.String) {
			this.code = code
		}
}
