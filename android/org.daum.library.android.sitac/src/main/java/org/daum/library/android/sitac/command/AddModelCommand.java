package org.daum.library.android.sitac.command;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.model.SITACEngine;
import org.daum.library.android.sitac.view.entity.IEntity;

public class AddModelCommand implements ICommand {

	private SITACEngine engine;

	public AddModelCommand(SITACEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
		if (args.length >= 2) {
			engine.add((IModel) args[0], (IEntity) args[1]);
		} else {
			engine.add((IModel) args[0], (IEntity) null);
		}
	}
}
