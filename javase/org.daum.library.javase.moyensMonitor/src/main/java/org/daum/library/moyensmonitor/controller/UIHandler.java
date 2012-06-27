package org.daum.library.moyensmonitor.controller;

import java.util.Date;
import java.util.Random;

import org.daum.common.model.api.Demand;
import org.daum.common.model.api.VehicleType;
import org.daum.common.util.api.TimeFormatter;
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

    @Override
    public void onAddFakeDemand() {
        VehicleType[] types = VehicleType.values();
        Random random = new Random();
        int randomIndex = random.nextInt(types.length);
        engine.add(new Demand(types[randomIndex]));
    }

    @Override
    public void onDeleteBtnClicked(Demand d) {
        engine.delete(d);
    }

    @Override
    public void onEditDemand(Demand d, String[] values) {
        if (values[4] != null && !values[4].isEmpty()) {
            d.setGh_crm(new Date());
        }

        if (values[5] != null && !values[5].isEmpty()) {
            d.setGh_engage(new Date());
        }

        if (values[6] != null && !values[6].isEmpty()) {
            d.setGh_desengagement(new Date());
        }
        engine.update(d);
    }
}
