package dev.aschim.mc.motdr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandMotdr implements CommandExecutor, TabCompleter {
    private final MOTDr plugin;
    private static final int PAGE_SIZE = 10;

    public CommandMotdr(MOTDr plugin) {
        plugin.getCommand("motdr").setExecutor(this);
        plugin.getCommand("motdr").setTabCompleter(this);
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length <= 1) {
            options.add("list");
            options.add("add");
            options.add("remove");
        } else if (args.length > 1) {
            List<String> messages = plugin.utils.getMessages();
            if (args[0].equalsIgnoreCase("list")) {
                final int numPages = (int) Math.ceil((double) messages.size() / PAGE_SIZE);
                for (int i = 0; i < numPages; i++) {
                    String page = Integer.toString(i + 1);
                    if (args[1].isBlank() || page.startsWith(args[1])) {
                        options.add(page);
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                for (int i = 0; i < messages.size(); i++) {
                    String num = Integer.toString(i + 1);
                    if (args[1].isBlank() || num.startsWith(args[1])) {
                        options.add(num);
                    }
                }
            }
        }
        return options;
    }

    private boolean listMessages(CommandSender sender, List<String> args) {
        int page = 1;
        if (!args.isEmpty()) {
            try {
                page = Integer.parseInt(args.get(0));
            } catch (Exception e) {
                // Do nothing, leave page at 1
            }
        }

        List<String> messages = plugin.utils.getMessages();
        final int numPages = (int) Math.ceil((double) messages.size() / PAGE_SIZE);

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
            for (int i = (page - 1) * PAGE_SIZE; i < page * PAGE_SIZE && i < messages.size(); i++) {
                sender.sendMessage(String.valueOf(i + 1) + ": " + messages.get(i));
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
                int index = Integer.parseInt(args.get(0)) - 1;
                String message = plugin.utils.removeMessage(index);
                if (message == null) {
                    return false;
                }
                sender.sendMessage("Removed MOTD: " + message);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
