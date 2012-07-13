package org.daum.library.android.sitac.view.entity;

import org.sitac.IModel;

public interface IEntityFactory {
	
	public IEntity build(IModel m);
}
