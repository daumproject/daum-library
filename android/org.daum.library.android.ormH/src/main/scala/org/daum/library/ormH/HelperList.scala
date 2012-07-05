package org.daum.library.ormH

import collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/07/12
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
 */


class HelperList {

  def convert(t: ListBuffer[Object]): java.util.List[Object] = {
    import scala.collection.JavaConversions._
    t
  }

}

