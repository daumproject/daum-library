package org.daum.library.moyensmonitor.controller;

import org.daum.library.moyensmonitor.model.MoyensEngine;
import org.daum.library.moyensmonitor.view.MoyensMonitorFrame;

public class MoyensMonitorController {

	private MoyensEngine engine;
	
	public MoyensMonitorController(MoyensMonitorFrame frame, String nodeName) {
		EngineHandler engineHandler = new EngineHandler(frame);
		this.engine = new MoyensEngine(nodeName, engineHandler);
		
		UIHandler uiHandler = new UIHandler(engine);
		frame.setOnMoyensMonitorEventListener(uiHandler);
	}

}
