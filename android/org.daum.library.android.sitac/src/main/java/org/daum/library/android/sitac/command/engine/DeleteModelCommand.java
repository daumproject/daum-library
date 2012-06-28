package org.daum.library.android.sitac.command.engine;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.engine.SITACEngine;
import org.daum.library.android.sitac.view.entity.IEntity;

public class DeleteModelCommand implements IEngineCommand {

	private SITACEngine engine;

	public DeleteModelCommand(SITACEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
		if (args.length == 1 && args[0] instanceof IModel) {
			engine.delete((IModel) args[0]);

		} else {
			throw new IllegalArgumentException(
					"DeleteModelCommand should be called like this: execute(IModel)");
		}
	}
}
