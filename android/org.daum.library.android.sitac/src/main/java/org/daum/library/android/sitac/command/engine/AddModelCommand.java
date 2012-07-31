package org.daum.library.android.sitac.command.engine;

import org.daum.library.android.sitac.engine.IEngine;
import org.daum.common.genmodel.*;

public class AddModelCommand implements IEngineCommand {

	private IEngine engine;

	public AddModelCommand(IEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
        if (args.length == 1 && args[0] instanceof IModel) {
			engine.add((IModel) args[0]);
			
		} else {
			throw new IllegalArgumentException(
					"AddModelCommand should be called like this: execute(IModel)");
		}
	}
}
