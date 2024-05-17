package fr.robotv2.spigotboilerplate.message;

import org.bukkit.command.CommandSender;

import java.util.Collection;

public interface MessageParser<T> {

    void send(CommandSender sender);

    default void send(Collection<? extends CommandSender> senders) {
        senders.forEach(this::send);
    }
}
