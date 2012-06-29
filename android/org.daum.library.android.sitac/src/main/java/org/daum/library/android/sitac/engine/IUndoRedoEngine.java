package org.daum.library.android.sitac.engine;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 29/06/12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public interface IUndoRedoEngine {

    public boolean canUndo();

    public boolean canRedo();
}
