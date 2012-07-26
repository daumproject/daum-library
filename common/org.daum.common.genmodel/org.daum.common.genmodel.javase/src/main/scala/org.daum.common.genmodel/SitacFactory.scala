package org.daum.common.genmodel;

import impl._ ;

object SitacFactory {

  def eINSTANCE = SitacFactory
  def getVersion = "1.0"

  def createIntervention : Intervention = new InterventionImpl
  def createDetachement : Detachement = new DetachementImpl
  def createMoyens : Moyens = new MoyensImpl
  def createVehicule : Vehicule = new VehiculeImpl
  def createInterventionType : CodeSinistre = new CodeSinistreImpl
  def createHistorique : Historique = new HistoriqueImpl
  def createEntree : Entree = new EntreeImpl
  def createTypeEntree : TypeEntree = new TypeEntreeImpl
  def createPosition : Position = new PositionImpl
  def createGpsPoint : GpsPoint = new GpsPointImpl
  def createPositionCivil : PositionCivil = new PositionCivilImpl
  def createPersonne : Personne = new PersonneImpl
  def createAgent : Agent = new AgentImpl
  def createAffectation : Affectation = new AffectationImpl
  def createSitacModel : SitacModel = new SitacModelImpl
  def createInfoTactic : InfoTactic = new InfoTacticImpl
  def createInfoPos : InfoPos = new InfoPosImpl
  def createPriseEau : PriseEau = new PriseEauImpl
  def createSourceDanger : SourceDanger = new SourceDangerImpl
  def createCategorie : Categorie = new CategorieImpl
  def createCible : Cible = new CibleImpl
  def createSinistre : Sinistre = new SinistreImpl
  def createInfoLignePos : InfoLignePos = new InfoLignePosImpl
  def createInfoZone : InfoZone = new InfoZoneImpl
  def createAction : Action = new ActionImpl
  def createArrowAction : ArrowAction = new ArrowActionImpl
  def createZoneAction : ZoneAction = new ZoneActionImpl
  def createInfoHorodate : InfoHorodate = new InfoHorodateImpl
  def createActionType : ActionType = new ActionTypeImpl
  def createInfoNiveau : InfoNiveau = new InfoNiveauImpl
  def createSecteurGeo : SecteurGeo = new SecteurGeoImpl
  def createSecteurFonctionnel : SecteurFonctionnel = new SecteurFonctionnelImpl
  def createInfoResponsable : InfoResponsable = new InfoResponsableImpl
  def createDonneeContextuel : DonneeContextuel = new DonneeContextuelImpl
  def createMessageAmbiance : MessageAmbiance = new MessageAmbianceImpl
  def createDatedValue : DatedValue = new DatedValueImpl

  final val classes: Array[Class[_]] = Array(classOf[ActionImpl], classOf[ActionTypeImpl], classOf[AffectationImpl], classOf[AgentImpl], classOf[CategorieImpl], classOf[CibleImpl], classOf[DetachementImpl], classOf[DonneeContextuelImpl], classOf[EntreeImpl], classOf[GpsPointImpl], classOf[HistoriqueImpl], classOf[InfoHorodateImpl], classOf[InfoLignePosImpl], classOf[InfoNiveauImpl], classOf[InfoPosImpl], classOf[InfoResponsableImpl], classOf[InfoTacticImpl], classOf[InfoZoneImpl], classOf[InterventionImpl], classOf[CodeSinistreImpl], classOf[MessageAmbianceImpl], classOf[MoyensImpl],classOf[PersonneImpl], classOf[PositionCivilImpl], classOf[PositionImpl], classOf[PriseEauImpl], classOf[SecteurFonctionnelImpl], classOf[SecteurGeoImpl], classOf[SinistreImpl], classOf[SitacModelImpl], classOf[SourceDangerImpl], classOf[TypeEntreeImpl],classOf[VehiculeImpl], classOf[ArrowActionImpl], classOf[ZoneActionImpl])
}
