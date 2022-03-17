package dev.aschim.mc.motdr;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("MOTDr Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MOTDr Disabled!");
    }
}
