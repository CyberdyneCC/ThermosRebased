package com.cyberdynecc.thermos.updater;

import com.cyberdynecc.thermos.Thermos;
import com.cyberdynecc.thermos.ThermosCommand;
import com.cyberdynecc.thermos.updater.TVersionRetriever.IVersionCheckCallback;
import net.minecraft.server.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class DefaultUpdateCallback implements IVersionCheckCallback {
    public static DefaultUpdateCallback INSTANCE;

    static {
        INSTANCE = new DefaultUpdateCallback();
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (mHasUpdate && hasPermission(player)) {
            sendUpdate(player);
        }
    }

    private boolean hasPermission(CommandSender player) {
        return player.hasPermission(ThermosCommand.UPDATE);
    }

    private void sendUpdate(CommandSender player) {
        CommandSenderUpdateCallback.newVersion(player, mCurrentVersion,
                mNewVersion);
    }

    private boolean mHasUpdate;
    private String mCurrentVersion;
    private String mNewVersion;

    private DefaultUpdateCallback() {
    }

    @Override
    public void upToDate(String version) {
        mHasUpdate = false;
        mCurrentVersion = version;
        mNewVersion = null;
    }

    @Override
    public void newVersion(String currentVersion, String newVersion) {
        mCurrentVersion = currentVersion;
        mNewVersion = newVersion;
        if (!mHasUpdate) {
            Bukkit.getConsoleSender().sendMessage("New version of Thermos available: " + newVersion);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (hasPermission(player)) {
                    sendUpdate(player);
                }
            }
        }
        mHasUpdate = true;
        if (MinecraftServer.thermosConfig.updatecheckerAutoinstall.getValue()
                && !mNewVersion.equals(Thermos.sNewServerVersion)
                && !Thermos.sUpdateInProgress) {
            Bukkit.getConsoleSender().sendMessage("Triggering auto update");
            ThermosUpdater.initUpdate(Bukkit.getConsoleSender(), newVersion);
        }
    }

    @Override
    public void error(Throwable t) {

    }
}
