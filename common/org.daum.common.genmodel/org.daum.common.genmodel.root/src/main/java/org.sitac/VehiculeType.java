package org.sitac;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/06/12
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public enum VehiculeType {
	
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
	
	public static VehiculeSector[] getSectors() {
		return VehiculeSector.values();
	}

	public static org.sitac.VehiculeType[] getValues(VehiculeSector sector) {
		switch (sector) {
			case ALIM:
				return new org.sitac.VehiculeType[] {CCGC, DA, MPR};

			case INC:
				return new org.sitac.VehiculeType[] {BEA, CAEM, CCFM, CCGCLC, EPS, FMOGP, FPT, VPRO, VAR};

			case COM:
				return new org.sitac.VehiculeType[] {VL, VLHR, VPL, VPHV, VTP, VTU, VCYNO};

			case SAP:
				return new org.sitac.VehiculeType[] {SAC_PS, VLSV, VLOS, VLS, VSAV, VSM, VSR};

			case RTN:
				return new org.sitac.VehiculeType[] {VRCB, VICB, VNRBC, VRAD};

			case CHEM:
				return new org.sitac.VehiculeType[] {PCM, VLCC, VLCGD, VLCS, VLCG};

			default:
				return null;
		}
	}

	public static VehiculeSector getSector(org.sitac.VehiculeType type) {
		switch (type) {
			case SAC_PS:
			case VLSV:
			case VLOS:
			case VLS:
			case VSAV:
			case VSM:
			case VSR:
				return VehiculeSector.SAP;
			case BEA:
			case CAEM:
			case CCFM:
			case CCGCLC:
			case EPS:
			case FMOGP:
			case FPT:
			case VPRO:
			case VAR:
				return VehiculeSector.INC;
			case CCGC:
			case DA:
			case MPR:
				return VehiculeSector.ALIM;
			case VL:
			case VLHR:
			case VPL:
			case VPHV:
			case VTP:
			case VTU:
			case VCYNO:
				return VehiculeSector.COM;
			case VRCB:
			case VICB:
			case VNRBC:
			case VRAD:
				return VehiculeSector.RTN;
			case PCM:
			case VLCC:
			case VLCGD:
			case VLCS:
			case VLCG:
				return VehiculeSector.CHEM;
			default:
				return null;
		}
	}
}