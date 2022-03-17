package dev.aschim.mc.motdr;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class CommandMotd implements CommandExecutor {
    private final MOTDr plugin;
    private final HashMap<UUID, PermissionAttachment> permsMap = new HashMap<>();

    public CommandMotd(MOTDr plugin) {
        plugin.getCommand("motd").setExecutor(this);
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
                final String perm = "motdr.join";
                PermissionAttachment perms = permsMap.get(player.getUniqueId());
                if (perms == null) {
                    perms = player.addAttachment(plugin);
                    permsMap.put(player.getUniqueId(), perms);
                }
                if (player.hasPermission(perm)) {
                    perms.unsetPermission(perm);
                    player.sendMessage("MOTD on join toggled OFF");
                } else {
                    perms.setPermission(perm, true);
                    player.sendMessage("MOTD on join toggled ON");
                }
                return true;
            } else if (sender instanceof ConsoleCommandSender console) {
                // TODO: Let console toggle this for players
                console.sendMessage("This command doesn't work from the console yet");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
