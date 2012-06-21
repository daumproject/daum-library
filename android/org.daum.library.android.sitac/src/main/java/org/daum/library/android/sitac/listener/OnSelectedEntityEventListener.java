package org.daum.library.android.sitac.listener;

public interface OnSelectedEntityEventListener {
	
	/**
	 * Called when the delete button of the associated view is clicked
	 */
	public void onDeleteButtonClicked();
	
	/**
	 * Called when the confirm button of the associated view is clicked
	 */
	public void onConfirmButtonClicked();
	
	/**
	 * Called when the undo button of the associated view is clicked
	 */
	public void onUndoButtonClicked();

	/**
	 * Called when the redo button of the associated view is clicked
	 */
	public void onRedoButtonClicked();
}
