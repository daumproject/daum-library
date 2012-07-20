package org.daum.common.genmodel.cloner
class ModelCloner {
	def clone[A](o : A) : A = {
		o match {
			case o : org.daum.common.genmodel.SitacModel => {
				val context = new java.util.IdentityHashMap[Object,Object]()
				o.getClonelazy(context)
				o.resolve(context).asInstanceOf[A]
			}
			case _ => null.asInstanceOf[A]
		}
	}
}
