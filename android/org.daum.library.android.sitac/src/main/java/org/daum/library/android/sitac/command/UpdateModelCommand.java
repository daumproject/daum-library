package org.daum.library.android.sitac.command;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.model.SITACEngine;
import org.daum.library.android.sitac.view.entity.IEntity;

public class UpdateModelCommand implements ICommand {

	private SITACEngine engine;

	public UpdateModelCommand(SITACEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
		engine.update((IModel) args[0], (IEntity) args[1]);
	}
}
