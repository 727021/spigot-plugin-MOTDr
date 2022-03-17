package dev.aschim.mc.motdr;

import java.util.List;
import java.util.Random;

public class MotdUtils {
    private final MOTDr plugin;
    private final Random random = new Random();

    public MotdUtils(MOTDr plugin) {
        this.plugin = plugin;
    }

    public String getRandomMessage() {
        List<String> messages = getMessages();
        if (messages.isEmpty())
            return null;
        return messages.get(random.nextInt(messages.size()));
    }

    public List<String> getMessages() {
        return plugin.config.getStringList("messages");
    }

    public String addMessage(String message) {
        List<String> messages = getMessages();
        messages.add(message);
        updateMessageList(messages);
        return message;
    }

    public String removeMessage(int index) {
        if (index < 0 || index >= getMessages().size())
            return null;
        List<String> messages = getMessages();
        String removed = messages.remove(index);
        updateMessageList(messages);
        return removed;
    }

    private void updateMessageList(List<String> messages) {
        plugin.config.set("messages", messages);
    }
}
