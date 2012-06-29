package org.daum.library.android.sitac.engine;

import java.util.ArrayList;
import java.util.Observer;

import org.daum.library.android.sitac.command.IUndoableCommand;
import org.daum.library.android.sitac.memento.IMemento;
import org.daum.library.android.sitac.observer.IObservable;
import org.daum.library.android.sitac.observer.MyObservable;

public class UndoRedoEngine implements IUndoRedoEngine, IObservable {
	
	private ArrayList<Pair> commands;
	private ArrayList<Pair> revCommands;
	private boolean saving;
	private int index;
    private MyObservable observable;
	
	public UndoRedoEngine() {
		saving = true;
		commands = new ArrayList<Pair>();
		revCommands = new ArrayList<Pair>();
		index = 0;

        observable = new MyObservable();
	}

	public void save(IUndoableCommand cmd) {
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
            notifyObservers();
		}
	}

	public void undo() {
		// disable command saving when undoing
		saving = false;
		index--;
		if (index < 0) index = 0;
		else {
			Pair p = revCommands.get(index);
			p.cmd.execute(p.mem);
		}
		// enable command saving when done undoing
		saving = true;
        notifyObservers();
	}

	public void redo() {
		// disable command saving when redoing
		saving = false;
		if (commands.size() != 0 && index <= commands.size()-1) {
			Pair p = commands.get(index);
			p.cmd.execute(p.mem);
			index++;
		}
		// enable command saving when done redoing
		saving = true;
        notifyObservers();
	}

    /**
     * Removes all saved commands from the engine stack
     */
    public void clearStack() {
        commands.clear();
        revCommands.clear();
        index = 0;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer obs) {
        observable.addObserver(obs);
    }

    @Override
    public void deleteObserver(Observer obs) {
        observable.deleteObserver(obs);
    }

    @Override
    public boolean canUndo() {
        return index > 0;
    }

    @Override
    public boolean canRedo() {
        return commands.size() != 0 && index <= commands.size()-1;
    }

    private void notifyObservers() {
        observable.setChanged();
        observable.notifyObservers(this);
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
