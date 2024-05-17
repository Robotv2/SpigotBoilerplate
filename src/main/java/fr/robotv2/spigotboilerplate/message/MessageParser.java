package fr.robotv2.spigotboilerplate.message;

import org.bukkit.command.CommandSender;

public interface MessageParser<T> {

    void send(CommandSender sender);

}
