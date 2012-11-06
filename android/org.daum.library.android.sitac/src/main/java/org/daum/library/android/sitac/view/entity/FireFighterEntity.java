package org.daum.library.android.sitac.view.entity;

import android.graphics.drawable.Drawable;
import org.daum.common.genmodel.IModel;
import org.daum.library.android.sitac.visitor.IVisitor;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/11/12
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class FireFighterEntity extends AbstractEntity
{
    private boolean destroyable = false;

    public FireFighterEntity(Drawable icon, String type) {
        super(icon, type);
    }

    @Override
    public void accept(IVisitor visitor, IModel m) {
        visitor.visit(this, m);
    }

    public void destroy() {
        this.destroyable = true;
        notifyObservers();
    }

    public boolean isDestroyable() {
        return destroyable;
    }

}
