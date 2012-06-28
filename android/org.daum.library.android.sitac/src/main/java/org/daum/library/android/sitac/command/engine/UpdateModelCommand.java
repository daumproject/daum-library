package org.daum.library.android.sitac.command.engine;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.engine.SITACEngine;
import org.daum.library.android.sitac.view.entity.IEntity;

public class UpdateModelCommand implements IEngineCommand {

	private SITACEngine engine;

	public UpdateModelCommand(SITACEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
        if (args.length == 1 && args[0] instanceof IModel) {
			engine.update((IModel) args[0]);
		} else {
			throw new IllegalArgumentException(
					"UpdateModelCommand should be called like this: execute(IModel)");

		}
	}
}
