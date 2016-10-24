package com.cyberdynecc.thermos;

import org.bukkit.configuration.file.YamlConfiguration;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.configuration.BoolSetting;
import net.minecraftforge.cauldron.configuration.ConfigBase;
import net.minecraftforge.cauldron.configuration.Setting;
import net.minecraftforge.cauldron.configuration.StringSetting;

public class ThermosConfig extends ConfigBase {
    public BoolSetting commandEnable = new BoolSetting(this, "command.enable",
            true, "Enable Thermos command");
    public BoolSetting updatecheckerEnable = new BoolSetting(this,
            "updatechecker.enable", true, "Enable Thermos update checker");
    public BoolSetting updatecheckerDeleteOld = new BoolSetting(this,
            "updatechecker.deleteOld", true, "Delete old version after update");
    public StringSetting updatecheckerSymlinks = new StringSetting(this,
            "updatechecker.symlinks", "", "(Re)create symlinks after update");
    public BoolSetting updatecheckerAutoinstall = new BoolSetting(this,
            "updatechecker.autoinstall", false, "Install updates without confirming");
    public BoolSetting updatecheckerQuite = new BoolSetting(this,
            "updatechecker.quite", false, "Print less info during update");
    public StringSetting updatecheckerInstallAs = new StringSetting(this,
            "updatechecker.installAs", "", "Install new version with specified name");

    public ThermosConfig() {
        super("thermos.yml", "th");
        register(commandEnable);
        register(updatecheckerEnable);
        register(updatecheckerDeleteOld);
        register(updatecheckerSymlinks);
        register(updatecheckerAutoinstall);
        register(updatecheckerQuite);
        register(updatecheckerInstallAs);
        load();
    }

    private void register(Setting<?> setting) {
        settings.put(setting.path, setting);
    }

    @Override
    public void registerCommands() {
        if (commandEnable.getValue()) {
            super.registerCommands();
        }
    }

    @Override
    protected void addCommands() {
        commands.put(commandName, new ThermosCommand());
    }

    @Override
    protected void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = "";
            for (Setting<?> toggle : settings.values()) {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: "
                            + toggle.def + " # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(
                        config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);
            save();
        } catch (Exception ex) {
            MinecraftServer.getServer().logSevere(
                    "Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}