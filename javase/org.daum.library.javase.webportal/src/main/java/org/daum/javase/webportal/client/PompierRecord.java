package org.daum.javase.webportal.client;

import org.daum.javase.webportal.shared.Agent;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PompierRecord extends ListGridRecord {  

	public PompierRecord() {  
	}  

	public PompierRecord(String nom, String prenom) {  
		setNom(nom);
		setPrenom(prenom);
	}  
	
	public PompierRecord(Agent agent){
        setId(agent.getId());
	    setNom(agent.getNom());
        setPrenom(agent.getPrenom());
        setMatricule(agent.getMatricule());
        setPassword(agent.getPassword());
	}

    public String getMatricule(){
        return getAttributeAsString("matricule");
    }

    public void setMatricule(String matricule) {
        setAttribute("matricule", matricule);
    }

    public String getPassword(){
        return getAttributeAsString("password");
    }

    public void setPassword(String password) {
        setAttribute("password", password);
    }

    public void setId(String id){
        setAttribute("id", id);
    }

    public String getId(){
        return getAttributeAsString("id");
    }
	
	public void setNom(String nom) {  
		setAttribute("nom", nom);  
	}  

	public String getContinent() {  
		return getAttributeAsString("nom");  
	}
	
	public void setPrenom(String prenom) {  
		setAttribute("prenom", prenom);  
	}  

	public String getPrenom() {  
		return getAttributeAsString("prenom");  
	}


}  


