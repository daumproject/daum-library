package org.daum.library.moyensmonitor.listener;

import org.daum.common.model.api.Demand;

public interface OnMoyensMonitorEventListener {

	void onValidateButtonClicked(Demand d, String cis);
}
