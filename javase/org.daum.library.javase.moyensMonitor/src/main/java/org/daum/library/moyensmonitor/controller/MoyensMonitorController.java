package org.daum.library.moyensmonitor.controller;

import org.daum.library.moyensmonitor.model.MoyensEngine;
import org.daum.library.moyensmonitor.view.MoyensMonitorFrame;
import org.daum.library.ormH.store.ReplicaStore;

public class MoyensMonitorController {

	private MoyensEngine engine;
	
	public MoyensMonitorController(MoyensMonitorFrame frame, String nodeName, ReplicaStore storeImpl) {
		EngineHandler engineHandler = new EngineHandler(frame);
		this.engine = new MoyensEngine(nodeName, storeImpl, engineHandler);
		
		UIHandler uiHandler = new UIHandler(engine);
		frame.setOnMoyensMonitorEventListener(uiHandler);
	}

}
