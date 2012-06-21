package org.daum.library.android.sitac.command;

import org.daum.library.android.sitac.engine.UndoRedoEngine;

public class UndoCommand implements ICommand {
	
	private UndoRedoEngine engine;
	
	public UndoCommand(UndoRedoEngine engine) {
		this.engine = engine;
	}

	@Override
	public <T> void execute(T... args) {
		engine.undo();
	}
}
