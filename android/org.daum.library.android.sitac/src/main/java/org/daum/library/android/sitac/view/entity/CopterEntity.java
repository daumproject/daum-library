package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;
import org.daum.common.genmodel.IModel;
import org.daum.library.android.sitac.visitor.IVisitor;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/11/12
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class CopterEntity extends AbstractEntity {


    public CopterEntity(Drawable icon, String type) {
        super(icon, type);
        setSelectable(true);
    }

    @Override
    public void accept(IVisitor visitor, IModel m) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
