package org.daum.library.android.sitac.engine;

import org.daum.library.android.sitac.listener.OnEngineStateChangeListener;
import org.daum.common.genmodel.*;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/07/12
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public interface IEngine {

    void add(IModel model);

    void update(IModel model);

    void delete(IModel model);

    void setHandler(OnEngineStateChangeListener engineHandler);
}
