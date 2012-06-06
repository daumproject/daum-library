package org.daum.library.android.moyens.model;

import org.daum.common.util.api.TimeFormatter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 31/05/12
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
public class Demand implements Serializable {

    public String agres;
    public String cis;
    public String gh_demande;
    public String gh_depart;
    public String gh_crm;
    public String gh_engage;
    public String gh_desengagement;

    public Demand(String agres, String cis, String gh_demande, String gh_depart, String gh_crm,
                  String gh_desengagement, String gh_engage) {
        this.agres = agres;
        this.cis = cis;
        this.gh_demande = gh_demande;
        this.gh_depart = gh_depart;
        this.gh_crm = gh_crm;
        this.gh_desengagement = gh_desengagement;
        this.gh_engage = gh_engage;
    }

    public Demand(VehicleType type) {
        this.agres = type.name();
        this.cis = "";
        this.gh_demande = TimeFormatter.getGroupeHoraire(new Date());
        this.gh_depart = "";
        this.gh_crm = "";
        this.gh_desengagement = "";
        this.gh_engage = "";
    }

    @Override
    public String toString() {
        return agres+"_"+cis+"_"+gh_crm+"_"+gh_demande+"_"+gh_depart+"_"+gh_desengagement+"_"+gh_engage;
    }
}
