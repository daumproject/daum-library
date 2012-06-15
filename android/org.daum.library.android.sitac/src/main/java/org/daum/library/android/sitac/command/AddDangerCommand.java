package org.daum.library.android.sitac.command;

import org.daum.common.model.api.Danger;
import org.daum.library.android.sitac.model.SITACEngine;

public class AddDangerCommand implements ICommand {

	private SITACEngine engine;
	
	public AddDangerCommand(SITACEngine engine) {
		this.engine = engine;
	}
	
	@Override
	public <T> void execute(T arg) {
		engine.addDanger((Danger) arg);
	}

}
