package fr.robotv2.spigotboilerplate.util;

import fr.robotv2.spigotboilerplate.items.ApplicableItem;
import fr.robotv2.spigotboilerplate.placeholders.*;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
        return apply(s -> SafePlaceholderAPI.parsePAPI(s, offlinePlayer));
    }

    public CommandList apply(String from, String to) {
        return apply(new InternalPlaceholder(from, to));
    }

    public CommandList apply(InternalPlaceholder placeholder) {
        return apply(placeholder::apply);
    }

    public <T> CommandList apply(ValuePlaceholder<T> placeholder, T value) {
        return apply(s -> placeholder.apply(s, value));
    }

    public <A, B> CommandList apply(RelationalValuePlaceholder<A, B> placeholder, A fst, B snd) {
        return apply(s -> placeholder.apply(s, fst, snd));
    }

    public <A, B, C> CommandList apply(TriRelationalValuePlaceholder<A, B, C> placeholder, A fst, B snd, C thd) {
        return apply(s -> placeholder.apply(s, fst, snd, thd));
    }

    public void execute(Player player) {

        apply("%player%", player.getName());

        for(String command : commands) {

            final String prefix = command.split(" ")[0];
            final String finalCommand = command.substring(prefix.length());

            switch (prefix.toUpperCase(Locale.ROOT)) {
                case "[PLAYER]":
                    Bukkit.dispatchCommand(player, finalCommand);
                    break;
                case "[CONSOLE]":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
                    break;
            }
        }
    }
}
