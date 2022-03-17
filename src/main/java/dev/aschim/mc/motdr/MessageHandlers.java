package dev.aschim.mc.motdr;

import java.util.List;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

public final class MessageHandlers implements Listener {
    private final MOTDr plugin;
    private final Random random = new Random();

    public MessageHandlers(MOTDr plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String message = randomMessage();
        if (plugin.config.getBoolean("showOnJoin") && message != null)
            event.getPlayer().sendMessage(message);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event) {
        String message = randomMessage();
        if (message != null)
            event.setMotd(message);
    }

    private String randomMessage() {
        List<String> messages = plugin.config.getStringList("messages");
        if (messages.isEmpty())
            return null;
        return messages.get(random.nextInt(messages.size()));
    }
}
