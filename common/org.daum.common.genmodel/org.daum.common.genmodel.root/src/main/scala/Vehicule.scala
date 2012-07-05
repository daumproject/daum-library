/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/07/12
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
package org.sitac;

import org.daum.library.ormH.annotations.Generated
import org.daum.library.ormH.annotations.Id
import org.daum.library.ormH.persistence.GeneratedType

trait Vehicule extends org.sitac.SitacContainer with  org.sitac.Materiel{

  private var vehiculetype : org.sitac.VehiculeType =null

  def getVehiculeType : org.sitac.VehiculeType = {
    vehiculetype
  }

  def setVehiculeType(vehiculetype : org.sitac.VehiculeType) {
    this.vehiculetype = vehiculetype
  }
}



