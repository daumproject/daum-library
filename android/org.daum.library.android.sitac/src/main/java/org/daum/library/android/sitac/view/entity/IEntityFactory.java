package org.daum.library.android.sitac.view.entity;

import org.daum.common.model.api.Danger;
import org.daum.common.model.api.Demand;
import org.daum.common.model.api.Target;

public interface IEntityFactory {

	public DemandEntity buildEntity(Demand d);
	
	public DangerEntity buildEntity(Danger d);

	public TargetEntity buildEntity(Target t);
	
	public Demand buildDemand(DemandEntity d);
	
	public Danger buildDanger(DangerEntity d);
	
	public Target buildTarget(TargetEntity t);
}
