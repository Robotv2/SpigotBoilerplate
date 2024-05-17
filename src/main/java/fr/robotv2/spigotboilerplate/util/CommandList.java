package fr.robotv2.spigotboilerplate.util;

import com.google.common.base.Enums;
import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import fr.robotv2.spigotboilerplate.placeholders.*;
import fr.robotv2.spigotboilerplate.placeholders.impl.InternalPlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.impl.RelationalValuePlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.impl.TriRelationalValuePlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.impl.ValuePlaceholder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@AllArgsConstructor
public class CommandList {

    private List<String> commands;

    private CommandList apply(Function<String, String> function) {
        this.commands =  commands.stream().map(function).toList();
        return this;
    }

    public CommandList color() {
        return apply(ColorUtil::color);
    }

    public CommandList papi(OfflinePlayer offlinePlayer) {
        return apply(text -> SafePlaceholderAPI.parsePAPI(text, offlinePlayer));
    }

    public CommandList apply(String from, String to) {
        return apply(new InternalPlaceholder(from, to));
    }

    public CommandList apply(InternalPlaceholder placeholder) {
        return apply(placeholder::apply);
    }

    public <T> CommandList apply(ValuePlaceholder<T> placeholder, T value) {
        return apply(text -> placeholder.apply(text, value));
    }

    public <A, B> CommandList apply(RelationalValuePlaceholder<A, B> placeholder, A fst, B snd) {
        return apply(text -> placeholder.apply(text, fst, snd));
    }

    public <A, B, C> CommandList apply(TriRelationalValuePlaceholder<A, B, C> placeholder, A fst, B snd, C thd) {
        return apply(text -> placeholder.apply(text, fst, snd, thd));
    }

    public void execute(Player player) {

        apply("%player%", player.getName());

        for(String command : commands) {

            final String prefix = command.split(" ")[0];
            final String argument = command.length() == prefix.length() ? command.trim() : command.substring(prefix.length() + 1).trim();

            switch (prefix.toUpperCase(Locale.ROOT)) {
                case "[PLAYER]":
                    Bukkit.dispatchCommand(player, argument);
                    break;
                case "[CONSOLE]":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), argument);
                    break;
                case "[MESSAGE]":
                    player.sendMessage(ColorUtil.color(argument));
                    break;
                case "[CLOSE]":
                    player.closeInventory();
                    break;
                case "[SOUND]":
                    final Sound sound = Enums.getIfPresent(Sound.class, argument.toUpperCase(Locale.ROOT)).orNull();

                    if(sound == null) {
                        SpigotBoilerplate.INSTANCE.getLogger().warning("Unknown song: " + argument);
                        break;
                    }

                    player.playSound(player.getLocation(), sound, 1, 1);
                    break;
            }
        }
    }
}
