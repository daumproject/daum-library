package org.daum.library.android.sitac.command.manager;

import org.daum.library.android.sitac.command.engine.AddModelCommand;
import org.daum.library.android.sitac.command.engine.DeleteModelCommand;
import org.daum.library.android.sitac.command.engine.IEngineCommand;
import org.daum.library.android.sitac.command.engine.UpdateModelCommand;
import org.daum.library.android.sitac.engine.IEngine;

public class EngineCmdManager extends AbstractCmdManager<IEngineCommand> {

	private static EngineCmdManager instance;

	private EngineCmdManager(IEngine engine) {
		commands.put(AddModelCommand.class, new AddModelCommand(engine));
		commands.put(UpdateModelCommand.class, new UpdateModelCommand(engine));
		commands.put(DeleteModelCommand.class, new DeleteModelCommand(engine));
	}

	public static EngineCmdManager getInstance(IEngine engine) {
		if (EngineCmdManager.instance == null) {
			EngineCmdManager.instance = new EngineCmdManager(engine);
		}
		return EngineCmdManager.instance;
	}
}
