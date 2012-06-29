package org.daum.library.android.sitac.command.manager;

import org.daum.library.android.sitac.command.ui.AddPointCommand;
import org.daum.library.android.sitac.command.ui.ClearUndoStackCommand;
import org.daum.library.android.sitac.command.ui.IUICommand;
import org.daum.library.android.sitac.command.ui.RemovePointCommand;
import org.daum.library.android.sitac.engine.UndoRedoEngine;


public class UICmdManager extends AbstractCmdManager<IUICommand> {

	private static UICmdManager instance;
	
	private UICmdManager(UndoRedoEngine engine) {
		commands.put(AddPointCommand.class, new AddPointCommand(engine));
		commands.put(RemovePointCommand.class, new RemovePointCommand(engine));
        commands.put(ClearUndoStackCommand.class, new ClearUndoStackCommand(engine));
	}

	public static UICmdManager getInstance(UndoRedoEngine engine) {
		if (instance == null) instance = new UICmdManager(engine);
		return instance;
	}
}
