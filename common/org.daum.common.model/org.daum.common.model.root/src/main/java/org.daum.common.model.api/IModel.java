package org.daum.common.model.api;

import java.io.Serializable;

import org.daum.common.gps.api.IGpsPoint;

public interface IModel extends Serializable {

    public String getId();

    public void setId(String id);

	public void setLocation(IGpsPoint gpsPoint);

	public IGpsPoint getLocation();
}
