package org.daum.common.genmodel

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitactest/1.0
 */
trait SitacContainer extends Serializable with IsSerializable {

	 private var internal_eContainer : SitacContainer = null
	 private var internal_unsetCmd : Option[()=>Any] = None
	def eContainer = internal_eContainer

	def setEContainer( container : SitacContainer, unsetCmd : Option[()=>Any] ) {
		val tempUnsetCmd = internal_unsetCmd
		internal_unsetCmd = None
		tempUnsetCmd.map{inCmd => inCmd() }
		this.internal_eContainer = container

		internal_unsetCmd = unsetCmd
	}
}
