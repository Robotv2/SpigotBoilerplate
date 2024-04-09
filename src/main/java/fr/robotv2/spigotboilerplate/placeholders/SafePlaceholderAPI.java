package fr.robotv2.spigotboilerplate.placeholders;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

@UtilityClass
public class SafePlaceholderAPI {

    public boolean isPlaceholderAPIEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public String parsePAPI(String text, OfflinePlayer offlinePlayer) {
        return isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(offlinePlayer, text) : text;
    }
}
