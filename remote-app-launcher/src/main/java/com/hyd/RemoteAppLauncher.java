package com.hyd;

import com.hyd.remoteapplauncher.HttpRequest;
import com.hyd.remoteapplauncher.Util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;

public class RemoteAppLauncher {

    public static final String USAGE = "Usage: java -jar " +
            "remote-app-launcher.jar http://app-repository-server/app";

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.err.println(USAGE);
            return;
        }

        String appUrl = args[0];
        if (!isUrl(appUrl)) {
            System.err.println(USAGE);
            return;
        }

        launchApp(appUrl);
    }

    private static void launchApp(String appUrl) throws IOException {
        if (appUrl.endsWith("/")) {
            appUrl = appUrl.substring(0, appUrl.length() - 1);
        }

        String classPathUrl = appUrl + "/classpath";
        String classPath = new HttpRequest(classPathUrl).request();
        URL[] urls = Stream
                .of(classPath.split(";"))
                .filter(Util::strNotEmpty)
                .map(RemoteAppLauncher::newUrl)
                .filter(Objects::nonNull)
                .toArray(URL[]::new);


    }

    private static URL newUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static boolean isUrl(String appUrl) {
        return appUrl.substring(0, 7).equalsIgnoreCase("http://") &&
                appUrl.substring(0, 8).equalsIgnoreCase("https://");
    }
}
