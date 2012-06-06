package org.daum.library.android.moyens.view.quickactionbar.listener;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/06/12
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public interface OnActionClickedListener {

    /**
     * Called when an action is clicked
     * @param tab the name of the current selected tab
     * @param action the name of the clicked action
     */
    void onActionClicked(String tab, String action);
}
