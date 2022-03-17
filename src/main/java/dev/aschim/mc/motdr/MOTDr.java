package dev.aschim.mc.motdr;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MOTDr extends JavaPlugin {
    FileConfiguration config = getConfig();
    Random random = new Random();

    @Override
    public void onEnable() {
        this.config.addDefault("messages", new ArrayList<String>());
        this.config.addDefault("showOnJoin", true);
        config.options().copyDefaults(true);
        saveConfig();

        new MessageHandlers(this);
    }

    @Override
    public void onDisable() {
        //
    }
}
