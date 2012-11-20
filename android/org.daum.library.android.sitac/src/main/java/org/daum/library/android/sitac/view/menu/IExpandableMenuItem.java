package org.daum.library.android.sitac.view.menu;

import android.graphics.drawable.Drawable;
import org.daum.library.android.sitac.view.entity.IEntity;

public interface IExpandableMenuItem {

    IEntity getEntity();
    Drawable getIcon();
    String getText();
    void setText(String text);
    void setIcon(Drawable icon);
}
