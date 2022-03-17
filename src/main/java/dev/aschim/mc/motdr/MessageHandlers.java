package dev.aschim.mc.motdr;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

public final class MessageHandlers implements Listener {
    private final MOTDr plugin;

    public MessageHandlers(MOTDr plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String message = plugin.utils.getRandomMessage();
        Player player = event.getPlayer();
        if (plugin.config.getBoolean("showOnJoin") && player.hasPermission("motdr.join"))
            player.sendMessage(message == null ? plugin.getServer().getMotd() : message);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event) {
        String message = plugin.utils.getRandomMessage();
        if (message != null)
            event.setMotd(message);
    }
}
