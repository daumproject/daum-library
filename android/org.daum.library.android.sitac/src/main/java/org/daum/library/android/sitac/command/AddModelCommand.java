package org.daum.library.android.sitac.command;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.model.SITACEngine;

public class AddModelCommand implements ICommand {

	private SITACEngine engine;

	public AddModelCommand(SITACEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T arg) {
		engine.add((IModel) arg);
	}
}
