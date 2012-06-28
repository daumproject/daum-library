package org.daum.library.android.sitac.visitor;

import org.daum.common.model.api.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 27/06/12
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
public interface IElement {

    public void accept(IVisitor visitor, IModel m);
}
