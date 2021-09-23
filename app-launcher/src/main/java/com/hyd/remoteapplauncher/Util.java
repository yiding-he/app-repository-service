package com.hyd.remoteapplauncher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static int copyStream(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

}
