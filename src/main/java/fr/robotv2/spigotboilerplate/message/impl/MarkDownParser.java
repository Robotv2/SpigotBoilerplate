package fr.robotv2.spigotboilerplate.message.impl;

import de.themoep.minedown.MineDown;
import fr.robotv2.spigotboilerplate.message.MessageParser;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MarkDownParser implements MessageParser<BaseComponent[]> {

    private List<BaseComponent[]> componentList;

    public MarkDownParser(List<String> messages) {
        this.componentList = messages.stream().map(MineDown::parse).toList();
    }

    @Override
    public void send(CommandSender sender) {
        if(sender instanceof Player) {
            for(BaseComponent[] components : componentList) {
                ((Player) sender).spigot().sendMessage(components);
            }
        } else {
            for(BaseComponent[] components : componentList) {
                sender.sendMessage(BaseComponent.toLegacyText(components));
            }
        }
    }
}
