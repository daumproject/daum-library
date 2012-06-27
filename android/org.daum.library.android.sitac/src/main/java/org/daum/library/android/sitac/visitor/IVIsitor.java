package org.daum.library.android.sitac.visitor;

import org.daum.common.model.api.IModel;
import org.daum.library.android.sitac.view.entity.DemandEntity;
import org.daum.library.android.sitac.view.entity.IEntity;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 27/06/12
 * Time: 09:56
 * To change this template use File | Settings | File Templates.
 */
public interface IVIsitor {

    public void visit(DemandEntity entity, IModel m);

    public void visit(IEntity entity, IModel m);
}