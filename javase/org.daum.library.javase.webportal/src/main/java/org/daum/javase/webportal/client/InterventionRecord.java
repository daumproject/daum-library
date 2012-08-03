package org.daum.javase.webportal.client;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import org.daum.javase.webportal.shared.Agent;
import org.daum.javase.webportal.shared.Intervention;

public class InterventionRecord extends ListGridRecord {

	public InterventionRecord() {
	}

	public InterventionRecord(String id, String code) {
		setId(id);
		setCode(code);
	}

    public InterventionRecord(Intervention intervention){
        setId(intervention.getId());
        setCode(intervention.getCodeSinistre());
        setAdresse(intervention.getAdresse());
        setIdRequerant(intervention.getIdRequerant());
        setDescription(intervention.getDescription());
    }

    private void setDescription(String description) {
        setAttribute("description", description);
    }

    private String getDescription(String description){
        return getAttributeAsString("description");
    }


    private void setIdRequerant(String idRequerant) {
        setAttribute("idRequerant", idRequerant);
    }

    private String getIdRequerant(String idRequerant){
        return getAttributeAsString("idRequerant");
    }

    private void setAdresse(String adresse) {
        setAttribute("adresse", adresse);
    }

    private String getAdresse(){
        return getAttributeAsString("adresse");
    }

    private void setCode(String code) {
        setAttribute("code", code);
    }

    private String getCode(){
        return getAttributeAsString("code");
    }


    public void setId(String id){
        setAttribute("id", id);
    }

    public String getId(){
        return getAttributeAsString("id");
    }
}  


