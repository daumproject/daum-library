package org.daum.library.android.sitac.command.ui;

import org.daum.library.android.sitac.engine.UndoRedoEngine;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 29/06/12
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class ClearUndoStackCommand implements IUICommand {

    private UndoRedoEngine engine;

    public ClearUndoStackCommand(UndoRedoEngine engine) {
        this.engine = engine;
    }

    @Override
    public <T> void execute(T... args) {
        engine.clearStack();
    }
}
