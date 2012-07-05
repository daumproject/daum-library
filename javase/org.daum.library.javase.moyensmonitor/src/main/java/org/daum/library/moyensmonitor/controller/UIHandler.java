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
        Random r = new Random();
        d.setNumber("0"+(r.nextInt(9)+1));
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
        for (int i=0; i<values.length; i++) {
            if (values[i] != null && !values[i].isEmpty()) {
                switch (i) {
                    case 4:
                        if (d.getGh_crm() == null) d.setGh_crm(new Date());
                        break;

                    case 5:
                        if (d.getGh_engage() == null) d.setGh_engage(new Date());
                        break;

                    case 6:
                        if (d.getGh_desengagement() == null) d.setGh_desengagement(new Date());
                        break;
                }
            }
        }
        engine.update(d);
    }
}
