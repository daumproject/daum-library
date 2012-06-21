package org.daum.library.android.sitac.manager;

import org.daum.library.android.sitac.command.ICommand;
import org.daum.library.android.sitac.command.RedoCommand;
import org.daum.library.android.sitac.command.UndoCommand;
import org.daum.library.android.sitac.engine.UndoRedoEngine;

public class CmdManager extends AbstractCmdManager<ICommand> {

	private static CmdManager instance;
	
	private CmdManager(UndoRedoEngine engine) {
		commands.put(UndoCommand.class, new UndoCommand(engine));
		commands.put(RedoCommand.class, new RedoCommand(engine));
	}
	
	public static CmdManager getInstance(UndoRedoEngine engine) {
		if (instance == null) instance = new CmdManager(engine);
		return instance;
	}
}
