/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 11/07/12
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */

package org.daum.common.genmodel

import java.util.Date


trait DatedValue extends org.daum.common.genmodel.SitacContainer with Capteurs
{
 var values :  java.util.HashMap[Date,Double] = new java.util.HashMap[Date,Double]
 var last :Date = null

  def addValue(value : Double) {
   val d : Date = new Date()
    values.put(d,value)
    last = d
  }

  def getValues() : java.util.HashMap[Date,Double] = {
    values
  }


def lastUpdate() : Date =  {
  last
}

}