package org.daum.library.android.sitac.command.ui;

import org.daum.library.android.sitac.command.IUndoableCommand;
import org.daum.library.android.sitac.engine.UndoRedoEngine;
import org.daum.library.android.sitac.memento.PointMemento;
import org.daum.library.android.sitac.memento.IMemento;
import org.daum.library.android.sitac.view.entity.AbstractShapedEntity;
import org.osmdroid.api.IGeoPoint;

public class AddPointCommand implements IUICommand, IUndoableCommand {

	private UndoRedoEngine undoRedoEngine;
	private AbstractShapedEntity shapedEnt;
	private IGeoPoint geoP;

	public AddPointCommand(UndoRedoEngine undoRedoEngine) {
		this.undoRedoEngine = undoRedoEngine;
	}

	@Override
	public <T> void execute(T... args) {
		if (args.length == 1 && args[0] instanceof PointMemento) {
			PointMemento m = (PointMemento) args[0];
			this.shapedEnt = m.entity;
			this.geoP = m.geoP;
			
		} else if (args.length == 2 && args[0] instanceof AbstractShapedEntity
				&& args[1] instanceof IGeoPoint) {
			this.shapedEnt = (AbstractShapedEntity) args[0];
			this.geoP = (IGeoPoint) args[1];

		} else {
			throw new IllegalArgumentException(
					"AddPointCommand should be called like this: execute(AbstractShapedEntity, IGeoPoint) or execute(PointMemento)");
		}

		shapedEnt.addPoint(geoP);
		undoRedoEngine.save(this);

		// discard field values for next executions
		shapedEnt = null;
		geoP = null;
	}

	@Override
	public IMemento saveMemento() {
		return new PointMemento(shapedEnt, geoP);
	}

	@Override
	public IUndoableCommand getReverseCommand() {
		return new RemovePointCommand(undoRedoEngine);
	}
}
