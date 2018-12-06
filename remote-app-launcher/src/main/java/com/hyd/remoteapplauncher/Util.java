package com.hyd.remoteapplauncher;

public class Util {

    public static boolean strEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean strNotEmpty(String s) {
        return !strEmpty(s);
    }

    public static String strRemoveEnd(String s, String end) {
        if (strEmpty(s) || strEmpty(end) || !s.endsWith(end)) {
            return s;
        } else {
            return s.substring(0, s.length() - end.length());
        }
    }
}
