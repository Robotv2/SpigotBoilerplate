package fr.robotv2.spigotboilerplate;

import de.themoep.minedown.MineDown;
import fr.robotv2.spigotboilerplate.placeholders.InternalPlaceholder;
import fr.robotv2.spigotboilerplate.util.ColorUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Messages {

    private final Map<String, String> cache = new HashMap<>();

    @Getter
    @Setter
    private FileConfiguration source;

    @Getter
    @Setter
    private String defaultPath = "";

    public void invalidateCache() {
        cache.clear();
    }

    public String getMessage(String path) {
        return source != null ? source.getString(defaultPath + path) : "Source is missing in Messages.";
    }

    public String getColorize(String path) {
        final String message;
        return (message = getMessage(path)) != null && !message.isEmpty() ? ColorUtil.color(message) : "NULL";
    }

    private String applyPlaceholders(String message, InternalPlaceholder... placeholders) {

        for(InternalPlaceholder placeholder : placeholders) {
            message = placeholder.apply(message);
        }

        return message;
    }

    private BaseComponent[] applyMineDown(String message, InternalPlaceholder... placeholders) {
        final Map<String, String> replacements = new HashMap<>();

        for(InternalPlaceholder placeholder : placeholders) {
            replacements.put(placeholder.getFrom(), placeholder.getTo());
        }

        return new MineDown(message).replace(replacements).toComponent();
    }

    public void sendPluginMessage(CommandSender sender, String path, InternalPlaceholder... placeholders) {

        String message = cache.computeIfAbsent(path, Messages::getColorize);

        if(message.equalsIgnoreCase("NULL")) {
            return;
        }

        message = applyPlaceholders(message, placeholders);

        if(sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(applyMineDown(message, placeholders));
        } else {
            sender.sendMessage(message);
        }
    }

    public void sendPluginMessage(Collection<? extends CommandSender> senders, String path, InternalPlaceholder... placeholders) {

        String message = cache.computeIfAbsent(path, Messages::getColorize);

        if(message.equalsIgnoreCase("NULL")) {
            return;
        }

        message = applyPlaceholders(message, placeholders);

        for(CommandSender sender : senders) {
            if(sender instanceof Player) {
                ((Player) sender).spigot().sendMessage(applyMineDown(message, placeholders));
            } else {
                sender.sendMessage(message);
            }
        }
    }
}
