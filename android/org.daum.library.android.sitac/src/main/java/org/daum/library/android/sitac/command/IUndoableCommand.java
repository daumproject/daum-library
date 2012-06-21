package org.daum.library.android.sitac.command;

import org.daum.library.android.sitac.memento.IMemento;

public interface IUndoableCommand extends ICommand {

	public IMemento saveMemento();
	
	public IUndoableCommand getReverseCommand();
}
