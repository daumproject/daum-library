package org.daum.library.moyensmonitor.controller;

import java.util.Date;

import org.daum.common.model.api.Demand;
import org.daum.library.moyensmonitor.listener.OnMoyensMonitorEventListener;
import org.daum.library.moyensmonitor.model.MoyensEngine;

public class UIHandler implements OnMoyensMonitorEventListener {

	private MoyensEngine engine;
	
	public UIHandler(MoyensEngine engine) {
		this.engine = engine;
	}

	@Override
	public void onValidateButtonClicked(Demand d, String cis) {
		d.setGh_depart(new Date());
		d.setCis(cis);
		engine.update(d);
	}
}
