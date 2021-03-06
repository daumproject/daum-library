package org.daum.common.model.api;

import java.util.Date;

import org.daum.common.gps.api.IGpsPoint;

import org.daum.library.ormH.annotations.Generated;
import org.daum.library.ormH.annotations.Id;
import org.daum.library.ormH.persistence.GeneratedType;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
public class Demand implements IModel {

	private static final long serialVersionUID = 7728877078263492496L;

    @Id
    @Generated(strategy = GeneratedType.UUID)
	private String id = "";
	private VehicleType type;
	private String number;
    private String cis;
    private Date gh_demande;
    private Date gh_depart;
    private Date gh_crm;
    private Date gh_engage;
    private Date gh_desengagement;
    private IGpsPoint location;

    public Demand(VehicleType type, String number, String cis, Date gh_demande, Date gh_depart, Date gh_crm,
    		Date gh_engage, Date gh_desengagement, IGpsPoint location) {
        this.type = type;
        this.number = number;
        this.cis = cis;
        this.gh_demande = gh_demande;
        this.gh_depart = gh_depart;
        this.gh_crm = gh_crm;
        this.gh_engage = gh_engage;
        this.gh_desengagement = gh_desengagement;
        this.location = location;
    }

	public Demand(VehicleType type) {
    	this.type = type;
        this.gh_demande = new Date();
    }

    @Override
	public String getId() {
		return id;
	}

    @Override
	public void setId(String id) {
		this.id = id;
	}

    @Override
    public IGpsPoint getLocation() {
		return location;
	}

    @Override
	public void setLocation(IGpsPoint location) {
		this.location = location;
	}
    
	public VehicleType getType() {
		return type;
	}

	public String getNumber() {
		return number;
	}

	public String getCis() {
		return cis;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setCis(String cis) {
		this.cis = cis;
	}

	public void setGh_demande(Date gh_demande) {
		this.gh_demande = gh_demande;
	}

	public void setGh_crm(Date gh_crm) {
		this.gh_crm = gh_crm;
	}

	public void setGh_engage(Date gh_engage) {
		this.gh_engage = gh_engage;
	}

	public void setGh_desengagement(Date gh_desengagement) {
		this.gh_desengagement = gh_desengagement;
	}

	public Date getGh_demande() {
		return gh_demande;
	}

	public Date getGh_depart() {
		return gh_depart;
	}
	
	public void setGh_depart(Date date) {
		this.gh_depart = date;
	}

	public Date getGh_crm() {
		return gh_crm;
	}

	public Date getGh_engage() {
		return gh_engage;
	}

	public Date getGh_desengagement() {
		return gh_desengagement;
	}

	@Override
	public String toString() {
		return "[" + id + "]"+type.name() + "_" + number + "_" + cis + "_" + gh_crm
				+ "_" + gh_demande + "_" + gh_depart + "_"
				+ gh_engage + "_" + gh_desengagement+ "_" +location;
	}
}
