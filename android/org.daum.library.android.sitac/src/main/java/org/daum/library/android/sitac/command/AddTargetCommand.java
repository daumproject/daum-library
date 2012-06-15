package org.daum.library.android.sitac.command;

import org.daum.common.model.api.Target;
import org.daum.library.android.sitac.model.SITACEngine;

public class AddTargetCommand implements ICommand {

	private SITACEngine engine;
	
	public AddTargetCommand(SITACEngine engine) {
		this.engine = engine;
	}
	
	@Override
	public <T> void execute(T arg) {
		engine.addTarget((Target) arg);
	}

}
