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

	public static VehicleSector getSector(VehicleType type) {
		switch (type) {
			case SAC_PS:
			case VLSV:
			case VLOS:
			case VLS:
			case VSAV:
			case VSM:
			case VSR:
				return VehicleSector.SAP;
			case BEA:
			case CAEM:
			case CCFM:
			case CCGCLC:
			case EPS:
			case FMOGP:
			case FPT:
			case VPRO:
			case VAR:
				return VehicleSector.INC;
			case CCGC:
			case DA:
			case MPR:
				return VehicleSector.ALIM;
			case VL:
			case VLHR:
			case VPL:
			case VPHV:
			case VTP:
			case VTU:
			case VCYNO:
				return VehicleSector.COM;
			case VRCB:
			case VICB:
			case VNRBC:
			case VRAD:
				return VehicleSector.RTN;
			case PCM:
			case VLCC:
			case VLCGD:
			case VLCS:
			case VLCG:
				return VehicleSector.CHEM;
			default:
				return null;
		}
	}
}