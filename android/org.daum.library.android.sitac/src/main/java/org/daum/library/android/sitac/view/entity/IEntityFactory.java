package org.daum.library.android.sitac.view.entity;

import org.daum.common.model.api.IModel;

public interface IEntityFactory {
	
	public IModel build(IEntity e);
	
	public IEntity build(IModel m);
}
