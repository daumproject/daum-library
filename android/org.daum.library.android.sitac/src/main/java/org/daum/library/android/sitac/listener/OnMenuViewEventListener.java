package org.daum.library.android.sitac.listener;

import org.daum.library.android.sitac.view.entity.IEntity;

public interface OnMenuViewEventListener {

	/**
	 * Called when an item is selected in the asssociated menuView
	 * 
	 * @param item a relevant new entity according to the selected menuItem
	 */
	void onMenuItemSelected(IEntity item);
}
