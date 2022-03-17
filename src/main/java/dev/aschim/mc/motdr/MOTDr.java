package dev.aschim.mc.motdr;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MOTDr extends JavaPlugin {
    public final FileConfiguration config = getConfig();
    public final MotdUtils utils = new MotdUtils(this);

    @Override
    public void onEnable() {
        // Initialize config file
        this.config.addDefault("messages", new ArrayList<String>());
        this.config.addDefault("showOnJoin", true);
        config.options().copyDefaults(true);
        saveConfig();

        // Initialize event listeners
        new MessageHandlers(this);

        // Initialize commands
        new CommandMotd(this);
        new CommandMotdr(this);
    }

    @Override
    public void onDisable() {
        // Save any changes that were made while the server was running
        saveConfig();
    }
}
