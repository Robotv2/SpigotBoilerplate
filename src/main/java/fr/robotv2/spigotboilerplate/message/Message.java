package fr.robotv2.spigotboilerplate.message;

import com.google.common.collect.LinkedListMultimap;
import fr.robotv2.spigotboilerplate.message.impl.MarkDownParser;
import fr.robotv2.spigotboilerplate.message.impl.StringMessageParser;
import fr.robotv2.spigotboilerplate.util.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@UtilityClass
public class Message {

    private final LinkedListMultimap<String, String> cache = LinkedListMultimap.create();

    @Getter
    private FileConfiguration source;

    @Getter
    @Setter
    private String defaultPath = "";

    public void setSource(FileConfiguration source) {
        invalidateCache();
        Message.source = source;
    }

    public void invalidateCache() {
        cache.clear();
    }

    @NotNull
    public List<String> getMessages(String path) {
        Objects.requireNonNull(source, "Message configuration source cannot be null");

        final String fullPath = defaultPath + path;

        if (cache.containsKey(fullPath)) {
            return cache.get(fullPath);
        }

        final List<String> message;

        if (source.isString(fullPath)) {
            message = Collections.singletonList(source.getString(fullPath));
        } else if (source.isList(fullPath)) {
            message = source.getStringList(fullPath);
        } else {
            message = Collections.emptyList();
        }

        cache.putAll(fullPath, message);
        return message;
    }

    @NotNull
    public List<String> getColorizedMessages(String path) {
        return new StringMessageParser(getMessages(path)).color().getMessages();
    }

    @Deprecated
    @Nullable
    public String getMessage(String path) {
        final List<String> messages = Message.getMessages(path);
        return messages.isEmpty() ? null : messages.get(0);
    }

    @Deprecated
    @Nullable
    public String getColorizedMessage(String path) {
        final String message = getMessage(path);
        return message != null && !message.isEmpty() ? ColorUtil.color(message) : null;
    }

    public StringMessageParser toMessageParser(String path) {
        return new StringMessageParser(getMessages(path));
    }

    public MarkDownParser toMarkDownParser(String path) {
        return new MarkDownParser(getMessages(path));
    }
}
