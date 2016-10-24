package com.cyberdynecc.thermos;

import com.cyberdynecc.thermos.updater.CommandSenderUpdateCallback;
import com.cyberdynecc.thermos.updater.ThermosUpdater;
import com.cyberdynecc.thermos.updater.TVersionRetriever;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ThermosCommand extends Command {
    public static final String NAME = "thermos";
    public static final String CHECK = NAME + ".check";
    public static final String UPDATE = NAME + ".update";

    public ThermosCommand() {
        super(NAME);

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("/%s check\n", NAME));
        builder.append(String.format("/%s update [version]\n", NAME));
        setUsage(builder.toString());

        setPermission("thermos");
    }

    public boolean testPermission(CommandSender target, String permission) {
        if (testPermissionSilent(target, permission)) {
            return true;
        }
        target.sendMessage(ChatColor.RED
                + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
        return false;
    }

    public boolean testPermissionSilent(CommandSender target, String permission) {
        if (!super.testPermissionSilent(target)) {
            return false;
        }
        for (String p : permission.split(";"))
            if (target.hasPermission(p))
                return true;
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel,
                           String[] args) {
        if (!testPermission(sender))
            return true;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Please specify action");
            return true;
        }
        String action = args[0];
        if ("check".equals(action)) {
            if (!testPermission(sender, CHECK))
                return true;
            sender.sendMessage(ChatColor.GREEN + "Initiated version check...");
            new TVersionRetriever(new CommandSenderUpdateCallback(sender),
                    false);
        } else if ("update".equals(action)) {
            ThermosUpdater.initUpdate(sender, args.length > 1 ? args[1]
                    : null);
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown action");
        }
        return true;
    }

}