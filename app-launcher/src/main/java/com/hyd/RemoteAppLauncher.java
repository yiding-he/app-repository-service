package com.hyd;

import com.hyd.remoteapplauncher.HttpRequest;
import com.hyd.remoteapplauncher.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoteAppLauncher {

    public static final String USAGE = "Usage: java -jar " +
            "remote-app-launcher.jar http://app-repository-server/app";

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.err.println("no args specified.\n" + USAGE);
            return;
        }

        String appUrl = args[0];
        if (!Util.isUrl(appUrl)) {
            System.err.println("arg '" + appUrl + "' is not an URL.\n" + USAGE);
            return;
        }

        String[] appArgs = args.length > 1 ? Util.subArray(args, 1) : new String[0];
        launchApp(appUrl, appArgs);
    }

    private static void launchApp(String appUrl, String[] appArgs) throws
            IOException, NoSuchMethodException, ClassNotFoundException,
            InvocationTargetException, IllegalAccessException {

        if (appUrl.endsWith("/")) {
            appUrl = appUrl.substring(0, appUrl.length() - 1);
        }

        List<URL> urls = parseClassPathUrls(appUrl);
        System.out.println("CLASS_PATH:");
        urls.forEach(url -> System.out.println("  " + url));

        String mainClassName = parseMainClassName(appUrl);

        if (Util.strNotEmpty(mainClassName)) {
            URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[0]));
            Thread.currentThread().setContextClassLoader(classLoader);
            Class<?> mainClass = classLoader.loadClass(mainClassName);
            mainClass.getMethod("main", String[].class).invoke(null, (Object) appArgs);

        } else {
            String url = urls.get(1).toString();
            System.out.println("downloading jar " + url + " ...");
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            new HttpRequest(url).download(fileName);
            new ProcessBuilder("java", "-jar", fileName).start();
            System.out.println("jar " + fileName + " started.");
        }
    }

    private static String parseMainClassName(String appUrl) {
        try {
            String mainClassUrl = appUrl + "/main-class";
            String mainClassName = new HttpRequest(mainClassUrl).request();
            System.out.println("Main class from " + mainClassUrl + " : '" + mainClassName + "'");
            return mainClassName;
        } catch (IOException e) {
            return null;
        }
    }

    private static List<URL> parseClassPathUrls(String appUrl) throws IOException {
        String classPath = new HttpRequest(appUrl + "/class-path").request();
        List<URL> urls = Stream
                .of(classPath.split(";"))
                .filter(Util::strNotEmpty)
                .map(jarFile -> appUrl + "/lib/" + jarFile)
                .map(RemoteAppLauncher::newUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        urls.add(0, new File("./conf").toURI().toURL());
        return urls;
    }

    private static URL newUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
