package org.daum.library.android.sitac.engine;

import java.util.ArrayList;

import org.daum.library.android.sitac.command.IUndoableCommand;
import org.daum.library.android.sitac.memento.IMemento;

public class UndoRedoEngine {
	
	private ArrayList<Pair> commands;
	private ArrayList<Pair> revCommands;
	private boolean saving;
	private int index;
	
	public UndoRedoEngine() {
		saving = true;
		commands = new ArrayList<Pair>();
		revCommands = new ArrayList<Pair>();
		index = 0;
	}

	public void save(IUndoableCommand cmd) {
		System.out.println("index start save = "+index);
		if (saving) {
			if (index <= commands.size()-1) {
				// erase from index to the end of the list
				for (int i=index; i<commands.size(); i++) {
					commands.remove(i);
					revCommands.remove(i);
				}
			}
			commands.add(index, new Pair(cmd, cmd.saveMemento()));
			IUndoableCommand revCmd = cmd.getReverseCommand();
			revCommands.add(index, new Pair(revCmd, cmd.saveMemento()));
			index++;
		}
		System.out.println("index after save = "+index);
	}

	public void undo() {
		// disable command saving when undoing
		saving = false;
		System.out.println("index before undo = "+index);
		index--;
		if (index < 0) index = 0;
		else {
			Pair p = revCommands.get(index);
			p.cmd.execute(p.mem);
		}
		System.out.println("index after undo = "+index);
		// enable command saving when done undoing
		saving = true;
	}

	public void redo() {
		// disable command saving when redoing
		saving = false;
		System.out.println("index redo start = "+index);
		if (commands.size() != 0 && index <= commands.size()-1) {
			Pair p = commands.get(index);
			p.cmd.execute(p.mem);
			index++;
		}
		System.out.println("index redo after = "+index);
		// enable command saving when do redoing
		saving = true;
	}
	
	private class Pair {
		private IUndoableCommand cmd;
		private IMemento mem;
		
		private Pair(IUndoableCommand cmd, IMemento mem) {
			this.cmd = cmd;
			this.mem = mem;
		}
	}
}
