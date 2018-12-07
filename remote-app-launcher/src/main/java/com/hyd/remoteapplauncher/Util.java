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

    @SuppressWarnings("unchecked")
    public static String[] subArray(String[] arr, int start) {
        String[] result = new String[arr.length - start];
        System.arraycopy(arr, start, result, 0, arr.length - start);
        return result;
    }

    public static boolean isUrl(String appUrl) {
        return appUrl.substring(0, 7).equalsIgnoreCase("http://") ||
                appUrl.substring(0, 8).equalsIgnoreCase("https://");
    }
}
