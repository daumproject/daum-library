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
 var values :  LRUMap[Date,Double] = new LRUMap[Date,Double](2)
 var last :Double = 0

  def addValue(value : Double) {
   val d : Date = new Date()
    values.put(d,value)
    last = value
  }

  def getValues() : LRUMap[Date,Double] = {
    values
  }


def lastUpdate() : Double =  {
  last
}

}