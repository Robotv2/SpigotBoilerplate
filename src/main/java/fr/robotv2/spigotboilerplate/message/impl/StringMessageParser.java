package fr.robotv2.spigotboilerplate.message.impl;

import fr.robotv2.spigotboilerplate.message.MessageParser;
import fr.robotv2.spigotboilerplate.placeholders.PlaceholderSupport;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
public class StringMessageParser extends PlaceholderSupport<StringMessageParser> implements MessageParser<String> {

    private List<String> messages;
    private boolean color = true;

    public StringMessageParser(List<String> messages) {
        this.messages = messages != null ? messages : Collections.emptyList();
    }

    @Override
    public StringMessageParser apply(Function<String, String> fromToFunction) {
        messages = messages.stream().map(fromToFunction).toList();
        return this;
    }

    public StringMessageParser disableColor() {
        this.color = false;
        return this;
    }

    public MarkDownParser toMarkdownParser() {
        return new MarkDownParser(messages);
    }

    @Override
    public void send(CommandSender sender) {

        if(color) {
            color();
        }

        messages.forEach(sender::sendMessage);
    }

    @Override
    public void send(Collection<? extends CommandSender> senders) {

        if(color) {
            color();
        }

        senders.forEach(sender -> {
            messages.forEach(sender::sendMessage);
        });
    }
}
