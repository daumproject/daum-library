package org.daum.library.android.sitac.view.entity;


import org.daum.common.genmodel.IModel;

public interface IEntityFactory {
	
	public IEntity build(IModel m);
}
