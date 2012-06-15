package org.daum.library.android.sitac.command;

import org.daum.common.model.api.Demand;
import org.daum.library.android.sitac.model.SITACEngine;

public class UpdateDemandCommand implements ICommand {

	private SITACEngine engine;

	public UpdateDemandCommand(SITACEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T arg) {
		engine.updateDemand((Demand) arg);
	}
}
