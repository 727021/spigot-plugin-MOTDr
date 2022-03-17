package dev.aschim.mc.motdr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class CommandMotd implements CommandExecutor, TabCompleter {
    private final MOTDr plugin;
    private final HashMap<UUID, PermissionAttachment> permsMap = new HashMap<>();

    public CommandMotd(MOTDr plugin) {
        plugin.getCommand("motd").setExecutor(this);
        plugin.getCommand("motd").setTabCompleter(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            String message = plugin.utils.getRandomMessage();
            sender.sendMessage(message == null ? plugin.getServer().getMotd() : message);
            return true;
        } else if (args[0].equalsIgnoreCase("toggle")) {
            if (sender instanceof Player player) {
                boolean toggle = togglePlayer(player);
                player.sendMessage("MOTD on join toggled " + (toggle ? "ON" : "OFF"));
                return true;
            } else if (sender instanceof ConsoleCommandSender console) {
                try {
                    String playerName = args[1];
                    List<Player> foundPlayers = Bukkit.matchPlayer(playerName);
                    if (foundPlayers.isEmpty()) {
                        console.sendMessage("No players found with name " + playerName);
                        return true;
                    }
                    foundPlayers.forEach(player -> {
                        boolean toggle = togglePlayer(player);
                        String message = "MOTD on join toggled " + (toggle ? "ON" : "OFF");
                        player.sendMessage(message);
                        console.sendMessage(message + " for " + player.getName());
                    });
                } catch (Exception e) {
                    console.sendMessage("Couldn't toggle MOTD on join for player");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length <= 1) {
            options.add("toggle");
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("toggle") && sender instanceof ConsoleCommandSender) {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                options.add(player.getName());
            });
        }
        return options;
    }

    private boolean togglePlayer(Player player) {
        PermissionAttachment perms = permsMap.get(player.getUniqueId());
        if (perms == null) {
            perms = player.addAttachment(plugin);
            permsMap.put(player.getUniqueId(), perms);
        }
        final String perm = "motdr.join";
        if (player.hasPermission(perm)) {
            perms.unsetPermission(perm);
            return false;
        } else {
            perms.setPermission(perm, true);
            return true;
        }
    }
}
