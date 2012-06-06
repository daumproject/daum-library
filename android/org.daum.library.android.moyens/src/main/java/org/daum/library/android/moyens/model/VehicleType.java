package org.daum.library.android.moyens.model;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 06/06/12
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class VehicleType {

    public enum SECTOR {
        SAP, INC, ALIM, COM, RTN, CHEM
    }

    public enum SAP {
        SAC_PS, VLSV, VLOS, VLS, VSAV, VSM, VSR
    }

    public enum INC {
       BEA, CAEM, CCFM, CCGCLC, EPS, FMOGP, FPT, VPRO, VAR
    }

    public enum ALIM {
        CCGC, DA, MPR
    }

    public enum COM {
        VL, VLHR, VPL, VPHV, VTP, VTU, VCYNO
    }

    public enum RTN {
        VRCB, VICB, VNRBC, VRAD
    }

    public enum CHEM {
        PCM, VLCC, VLCGD, VLCS, VLCG
    }

    private static String TYPE = null;
    private static VehicleType INSTANCE = new VehicleType();

    public static String name() {
        return VehicleType.TYPE;
    }

    public static VehicleType valueOf(String type) {
        Exception e;

        try {
            SAP s = SAP.valueOf(type);
            VehicleType.TYPE = s.name();
            return VehicleType.INSTANCE;

        } catch (Exception tmp) { e = tmp; }

        try {
            INC s = INC.valueOf(type);
            VehicleType.TYPE = s.name();
            return VehicleType.INSTANCE;

        } catch (Exception tmp) { e = tmp; }

        try {
            ALIM s = ALIM.valueOf(type);
            VehicleType.TYPE = s.name();
            return VehicleType.INSTANCE;

        } catch (Exception tmp) { e = tmp; }

        try {
            COM s = COM.valueOf(type);
            VehicleType.TYPE = s.name();
            return VehicleType.INSTANCE;

        } catch (Exception tmp) { e = tmp; }

        try {
            RTN s = RTN.valueOf(type);
            VehicleType.TYPE = s.name();
            return VehicleType.INSTANCE;

        } catch (Exception tmp) { e = tmp; }

        try {
            CHEM s = CHEM.valueOf(type);
            VehicleType.TYPE = s.name();
            return VehicleType.INSTANCE;

        } catch (Exception tmp) { e = tmp; }

        throw new IllegalArgumentException(e);
    }

    public static Enum<?>[] types(SECTOR sector) {
        switch (sector) {
            case ALIM:
                return ALIM.values();

            case CHEM:
                return CHEM.values();

            case COM:
                return COM.values();

            case INC:
                return INC.values();

            case RTN:
                return RTN.values();

            case SAP:
                return SAP.values();

            default:
                return new Enum<?>[0];
        }
    }
}
