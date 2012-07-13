package org.daum.library.android.sitac.command.engine;

import org.daum.library.android.sitac.engine.IEngine;
import org.sitac.IModel;

public class DeleteModelCommand implements IEngineCommand {

	private IEngine engine;

	public DeleteModelCommand(IEngine engine) {
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
