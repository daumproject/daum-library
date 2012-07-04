package org.sitac;

/**
 * Created by Ecore Model Generator.
 * @authors: Gregory NAIN, Fouquet Francois
 * Date: 04 juil. 12 Time: 09:48
 * Meta-Model:NS_URI=http://sitac/1.0
 */
trait Intervention extends org.sitac.SitacContainer {
		private var numeroIntervention : java.lang.String = ""

		private var precision : java.lang.String = ""

		private var `type` : org.sitac.InterventionType = _

		private var historique : Option[org.sitac.Historique] = None

		private var position : Option[org.sitac.Position] = None

		private var requerant : Option[org.sitac.Personne] = None

		private lazy val detachements : scala.collection.mutable.ListBuffer[org.sitac.Detachement] = new scala.collection.mutable.ListBuffer[org.sitac.Detachement]()

		private lazy val victimes : scala.collection.mutable.ListBuffer[org.sitac.Personne] = new scala.collection.mutable.ListBuffer[org.sitac.Personne]()

		private lazy val infoTactics : scala.collection.mutable.ListBuffer[org.sitac.InfoTactic] = new scala.collection.mutable.ListBuffer[org.sitac.InfoTactic]()


		def getNumeroIntervention : java.lang.String = {
			numeroIntervention
		}

		def setNumeroIntervention(numeroIntervention : java.lang.String) {
			this.numeroIntervention = numeroIntervention
		}

		def getPrecision : java.lang.String = {
			precision
		}

		def setPrecision(precision : java.lang.String) {
			this.precision = precision
		}

		def getType : org.sitac.InterventionType = {
				`type`
		}

		def setType(`type` : org.sitac.InterventionType ) {
				this.`type` = (`type`)

		}

		def getHistorique : Option[org.sitac.Historique] = {
				historique
		}

		def setHistorique(historique : Option[org.sitac.Historique] ) {
if(this.historique!= historique){
				this.historique = (historique)
				historique.map{ dic=>				dic.setEContainer(this, Some(() => { this.historique= None }) )
				}}

		}

		def getPosition : Option[org.sitac.Position] = {
				position
		}

		def setPosition(position : Option[org.sitac.Position] ) {
if(this.position!= position){
				this.position = (position)
				position.map{ dic=>				dic.setEContainer(this, Some(() => { this.position= None }) )
				}}

		}

		def getRequerant : Option[org.sitac.Personne] = {
				requerant
		}

		def setRequerant(requerant : Option[org.sitac.Personne] ) {
				this.requerant = (requerant)

		}

		def getDetachements : List[org.sitac.Detachement] = {
				detachements.toList
		}
		def getDetachementsForJ : java.util.List[org.sitac.Detachement] = {
				import scala.collection.JavaConversions._
				detachements
		}

		def setDetachements(detachements : List[org.sitac.Detachement] ) {
if(this.detachements!= detachements){
				this.detachements.clear()
				this.detachements.insertAll(0,detachements)
				detachements.foreach{el=>
					el.setEContainer(this,Some(()=>{this.removeDetachements(el)}))
				}
}

		}

		def addDetachements(detachements : org.sitac.Detachement) {
				detachements.setEContainer(this,Some(()=>{this.removeDetachements(detachements)}))
				this.detachements.append(detachements)
		}

		def addAllDetachements(detachements : List[org.sitac.Detachement]) {
				detachements.foreach{ elem => addDetachements(elem)}
		}

		def removeDetachements(detachements : org.sitac.Detachement) {
				if(this.detachements.size != 0 && this.detachements.indexOf(detachements) != -1 ) {
						this.detachements.remove(this.detachements.indexOf(detachements))
						detachements.setEContainer(null,None)
				}
		}

		def removeAllDetachements() {
				this.detachements.foreach{ elem => removeDetachements(elem)}
		}

		def getVictimes : List[org.sitac.Personne] = {
				victimes.toList
		}
		def getVictimesForJ : java.util.List[org.sitac.Personne] = {
				import scala.collection.JavaConversions._
				victimes
		}

		def setVictimes(victimes : List[org.sitac.Personne] ) {
				this.victimes.clear()
				this.victimes.insertAll(0,victimes)

		}

		def addVictimes(victimes : org.sitac.Personne) {
				this.victimes.append(victimes)
		}

		def addAllVictimes(victimes : List[org.sitac.Personne]) {
				victimes.foreach{ elem => addVictimes(elem)}
		}

		def removeVictimes(victimes : org.sitac.Personne) {
				if(this.victimes.size != 0 && this.victimes.indexOf(victimes) != -1 ) {
						this.victimes.remove(this.victimes.indexOf(victimes))
				}
		}

		def removeAllVictimes() {
				this.victimes.foreach{ elem => removeVictimes(elem)}
		}

		def getInfoTactics : List[org.sitac.InfoTactic] = {
				infoTactics.toList
		}
		def getInfoTacticsForJ : java.util.List[org.sitac.InfoTactic] = {
				import scala.collection.JavaConversions._
				infoTactics
		}

		def setInfoTactics(infoTactics : List[org.sitac.InfoTactic] ) {
if(this.infoTactics!= infoTactics){
				this.infoTactics.clear()
				this.infoTactics.insertAll(0,infoTactics)
				infoTactics.foreach{el=>
					el.setEContainer(this,Some(()=>{this.removeInfoTactics(el)}))
				}
}

		}

		def addInfoTactics(infoTactics : org.sitac.InfoTactic) {
				infoTactics.setEContainer(this,Some(()=>{this.removeInfoTactics(infoTactics)}))
				this.infoTactics.append(infoTactics)
		}

		def addAllInfoTactics(infoTactics : List[org.sitac.InfoTactic]) {
				infoTactics.foreach{ elem => addInfoTactics(elem)}
		}

		def removeInfoTactics(infoTactics : org.sitac.InfoTactic) {
				if(this.infoTactics.size != 0 && this.infoTactics.indexOf(infoTactics) != -1 ) {
						this.infoTactics.remove(this.infoTactics.indexOf(infoTactics))
						infoTactics.setEContainer(null,None)
				}
		}

		def removeAllInfoTactics() {
				this.infoTactics.foreach{ elem => removeInfoTactics(elem)}
		}
def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
		val selfObjectClone = SitacFactory.createIntervention
		selfObjectClone.setNumeroIntervention(this.getNumeroIntervention)
		selfObjectClone.setPrecision(this.getPrecision)
		subResult.put(this,selfObjectClone)
		this.getHistorique.map{ sub =>
			sub.getClonelazy(subResult)
		}

		this.getPosition.map{ sub =>
			sub.getClonelazy(subResult)
		}

		this.getDetachements.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

		this.getInfoTactics.foreach{ sub => 
			sub.getClonelazy(subResult)
		}

	}
def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Intervention = {
		val clonedSelfObject = addrs.get(this).asInstanceOf[org.sitac.Intervention]
		clonedSelfObject.setType(addrs.get(this.getType).asInstanceOf[org.sitac.InterventionType])

		this.getHistorique.map{sub =>
			clonedSelfObject.setHistorique(Some(addrs.get(sub).asInstanceOf[org.sitac.Historique]))
		}

		this.getPosition.map{sub =>
			clonedSelfObject.setPosition(Some(addrs.get(sub).asInstanceOf[org.sitac.Position]))
		}

		this.getRequerant.map{sub =>
			clonedSelfObject.setRequerant(Some(addrs.get(sub).asInstanceOf[org.sitac.Personne]))
		}

		this.getDetachements.foreach{sub =>
			clonedSelfObject.addDetachements(addrs.get(sub).asInstanceOf[org.sitac.Detachement])
		}

		this.getVictimes.foreach{sub =>
			clonedSelfObject.addVictimes(addrs.get(sub).asInstanceOf[org.sitac.Personne])
		}

		this.getInfoTactics.foreach{sub =>
			clonedSelfObject.addInfoTactics(addrs.get(sub).asInstanceOf[org.sitac.InfoTactic])
		}

		this.getHistorique.map{ sub =>
			sub.resolve(addrs)
		}

		this.getPosition.map{ sub =>
			sub.resolve(addrs)
		}

		this.getDetachements.foreach{ sub => 
			sub.resolve(addrs)
		}

		this.getInfoTactics.foreach{ sub => 
			sub.resolve(addrs)
		}

		clonedSelfObject
	}

}
