package dev.aschim.mc.motdr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandMotdr implements CommandExecutor {
    private final MOTDr plugin;

    public CommandMotdr(MOTDr plugin) {
        plugin.getCommand("motdr").setExecutor(this);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        if (!argsList.isEmpty()) {
            String option = argsList.remove(0).toLowerCase();
            switch (option) {
                case "list":
                    return listMessages(sender, argsList);
                case "add":
                    return addMessage(sender, argsList);
                case "remove":
                    return removeMessage(sender, argsList);
                default:
                    return false;
            }
        }
        return false;
    }

    private boolean listMessages(CommandSender sender, List<String> args) {
        final int pageSize = 10;
        int page = 1;
        if (!args.isEmpty()) {
            try {
                page = Integer.parseInt(args.get(0));
            } catch (Exception e) {
                // Do nothing, leave page at 1
            }
        }

        List<String> messages = plugin.utils.getMessages();
        final int numPages = (int) Math.ceil((double) messages.size() / pageSize);

        if (page < 1) {
            page = 1;
        }
        if (page > numPages) {
            page = numPages;
        }

        if (messages.isEmpty()) {
            sender.sendMessage("No MOTDs found");
        } else {
            sender.sendMessage("MOTD List: (Page " + page + " of " + numPages + ")");
            for (int i = (page - 1) * pageSize; i < page * pageSize && i < messages.size(); i++) {
                sender.sendMessage(String.valueOf(i) + ": " + messages.get(i));
            }
        }
        return true;
    }

    private boolean addMessage(CommandSender sender, List<String> args) {
        String message = String.join(" ", args);
        if (message.isBlank()) {
            return false;
        }
        message = plugin.utils.addMessage(message);
        sender.sendMessage("Added MOTD: " + message);
        return true;
    }

    private boolean removeMessage(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            return false;
        } else {
            try {
                int index = Integer.parseInt(args.get(0));
                String message = plugin.utils.removeMessage(index);
                sender.sendMessage("Removed MOTD: " + message);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
