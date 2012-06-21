package org.daum.library.android.sitac.command;

import org.daum.library.android.sitac.engine.UndoRedoEngine;

public class RedoCommand implements ICommand {
	
	private UndoRedoEngine engine;
	
	public RedoCommand(UndoRedoEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
		engine.redo();
	}
}
