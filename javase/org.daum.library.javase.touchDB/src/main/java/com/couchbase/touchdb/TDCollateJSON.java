package com.couchbase.touchdb;

import com.couchbase.touchdb.sqlite.SQLiteDatabase;

import java.text.Collator;



public class TDCollateJSON {


    public static void registerCustomCollators(SQLiteDatabase database) {
        nativeRegisterCustomCollators(database, 16);
    }

    public static int compareStringsUnicode(String a, String b) {
        Collator c = Collator.getInstance();
        int res = c.compare(a, b);
        return res;
    }

    private static native void nativeRegisterCustomCollators(SQLiteDatabase database, int sdkVersion);

    //FIXME only public for now until tests are moved int same package
    public static native int testCollateJSON(int mode, int len1, String string1, int len2, String string2);
    public static native char testEscape(String source);
    public static native int testDigitToInt(int digit);


}
