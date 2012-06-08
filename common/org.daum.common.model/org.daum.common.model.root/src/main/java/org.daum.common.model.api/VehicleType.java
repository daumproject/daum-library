package org.daum.common.model.api;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/06/12
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public enum VehicleType {
	
	// SAP
	SAC_PS, VLSV, VLOS, VLS, VSAV, VSM, VSR,
	
	// INC
	BEA, CAEM, CCFM, CCGCLC, EPS, FMOGP, FPT, VPRO, VAR,
	
	// ALIM
	CCGC, DA, MPR,
	
	// COM
	VL, VLHR, VPL, VPHV, VTP, VTU, VCYNO,
	
	// RTN
	VRCB, VICB, VNRBC, VRAD,
	
	// CHEM
	PCM, VLCC, VLCGD, VLCS, VLCG;
	
	public static VehicleSector[] getSectors() {
		return VehicleSector.values();
	}

	public static VehicleType[] getValues(VehicleSector sector) {
		switch (sector) {
			case ALIM:
				return new VehicleType[] {CCGC, DA, MPR};
				
			case INC:
				return new VehicleType[] {BEA, CAEM, CCFM, CCGCLC, EPS, FMOGP, FPT, VPRO, VAR};
				
			case COM:
				return new VehicleType[] {VL, VLHR, VPL, VPHV, VTP, VTU, VCYNO};
				
			case SAP:
				return new VehicleType[] {SAC_PS, VLSV, VLOS, VLS, VSAV, VSM, VSR};
				
			case RTN:
				return new VehicleType[] {VRCB, VICB, VNRBC, VRAD};
				
			case CHEM:
				return new VehicleType[] {PCM, VLCC, VLCGD, VLCS, VLCG};
			
			default:
				return null;
		}
	}
}