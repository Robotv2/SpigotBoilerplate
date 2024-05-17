package fr.robotv2.spigotboilerplate;

import fr.robotv2.spigotboilerplate.message.Message;
import fr.robotv2.spigotboilerplate.placeholders.impl.InternalPlaceholder;
import fr.robotv2.spigotboilerplate.util.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
@Deprecated
public class Messages {

    private final Map<String, String> cache = new HashMap<>();

    @Getter
    private FileConfiguration source;

    @Getter
    private String defaultPath = "";

    public void setSource(FileConfiguration source) {
        Message.setSource(source);
    }

    public void setDefaultPath(String defaultPath) {
        Message.setDefaultPath(defaultPath);
    }

    public void invalidateCache() {
        Message.invalidateCache();
    }

    public String getMessage(String path) {
        final List<String> messages = Message.getMessages(path);
        return source != null ? source.getString(defaultPath + path) : "Source is missing in Messages.";
    }

    public String getColorize(String path) {
        final String message;
        return (message = getMessage(path)) != null && !message.isEmpty() ? ColorUtil.color(message) : "NULL";
    }

    public void sendPluginMessage(CommandSender sender, String path, InternalPlaceholder... placeholders) {
    }

    public void sendPluginMessage(Collection<? extends CommandSender> senders, String path, InternalPlaceholder... placeholders) {
    }
}
