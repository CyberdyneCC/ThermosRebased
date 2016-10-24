package com.cyberdynecc.thermos.updater;

import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;

import com.cyberdynecc.thermos.Thermos;
import com.cyberdynecc.thermos.TLog;
import net.minecraft.server.MinecraftServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TVersionRetriever implements Runnable, UncaughtExceptionHandler {
    private static final boolean DEBUG;
    private static final TLog sLogger;
    private static final JSONParser sParser;
    private static final String sCurrentVersion;
    private static MinecraftServer sServer;

    static {
        DEBUG = false;
        sLogger = TLog.get(TVersionRetriever.class.getSimpleName());

        sCurrentVersion = Thermos.getCurrentVersion();
        sParser = new JSONParser();
    }

    public static void init(MinecraftServer server) {
        sServer = server;
        if (MinecraftServer.thermosConfig.updatecheckerEnable.getValue()) {
            new TVersionRetriever(DefaultUpdateCallback.INSTANCE, true);
        }
    }

    private final IVersionCheckCallback mCallback;
    private final boolean mLoop;
    private final Thread mThread;

    public TVersionRetriever(IVersionCheckCallback callback, boolean loop) {
        if (DEBUG)
            sLogger.info("Created new version retriever");
        mCallback = callback;
        mLoop = loop;
        mThread = new Thread(this);
        mThread.setName("Thermos version retriever");
        mThread.setPriority(Thread.MIN_PRIORITY);
        mThread.setDaemon(true);
        mThread.setUncaughtExceptionHandler(this);
        mThread.start();
    }

    @Override
    public void run() {
        while (!mThread.isInterrupted()) {
            check();
            if (!mLoop)
                break;
            try {
                Thread.sleep(1000 * 60 * 10);// Sleep ten minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void check() {
        if (DEBUG)
            sLogger.info("Requesting for new version...");
        try {

            // TODO - update to new link for Thermos
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://prok.pw/version/pw.prok/Thermos")
                    .addParameter("hostname", sServer.getHostname())
                    .addParameter("port", "" + sServer.getPort()).build();
            HttpResponse response = HttpClientBuilder.create()
                    .setUserAgent("Thermos Version Retriever").build()
                    .execute(request);
            JSONObject json = (JSONObject) sParser.parse(new InputStreamReader(
                    response.getEntity().getContent()));
            String version = (String) json.get("version");
            if (DEBUG) {
                sLogger.info("Got the latest version: %s", version);
                sLogger.info("Current version is %s", sCurrentVersion);
            }
            if (!sCurrentVersion.equals(version)) {
                mCallback.newVersion(sCurrentVersion, version);
            } else {
                mCallback.upToDate(version);
            }
        } catch (Exception e) {
            uncaughtException(null, e);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        sLogger.warning(e, "Error occurred during retrieving version");
        if (mCallback != null) {
            mCallback.error(e);
        }
    }

    public interface IVersionCheckCallback {
        void upToDate(String version);

        void newVersion(String currentVersion, String newVersion);

        void error(Throwable t);
    }
}
