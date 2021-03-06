package com.cyberdynecc.thermos;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;

public class Thermos {
    private static boolean sManifestParsed = false;

    private static void parseManifest() {
        if (sManifestParsed) return;
        sManifestParsed = true;

        try {
            Enumeration<URL> resources = Thermos.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            Properties manifest = new Properties();
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                manifest.load(url.openStream());
                String version = manifest.getProperty("Thermos-Version");
                if (version != null) {
                    String path = url.getPath();
                    String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
                    jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
                    sServerLocation = new File(jarFilePath);

                    sCurrentVersion = version;
                    sBranch = manifest.getProperty("Thermos-Branch");
                    sChannel = manifest.getProperty("Thermos-Channel");
                }
                manifest.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String sCurrentVersion;

    public static String getCurrentVersion() {
        parseManifest();
        return sCurrentVersion;
    }

    private static File sServerLocation;

    public static File getServerLocation() {
        parseManifest();
        return sServerLocation;
    }

    private static String sBranch;

    public static String getBranch() {
        parseManifest();
        return sBranch;
    }

    private static String sChannel;

    public static String getChannel() {
        parseManifest();
        return sChannel;
    }

    public static File sNewServerLocation;
    public static String sNewServerVersion;
    public static boolean sUpdateInProgress;
}