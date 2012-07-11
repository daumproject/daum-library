/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 11/07/12
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */

package org.sitac

import java.util.Date


trait Temperature extends org.sitac.SitacContainer with Capteurs{

 var values :  java.util.HashMap[Date,Double] = new java.util.HashMap[Date,Double]

  def addValue(value : Double) {
    values.put(new Date(),value)
  }

  def getTemperatures() : java.util.HashMap[Date,Double] = {
    values
  }


}