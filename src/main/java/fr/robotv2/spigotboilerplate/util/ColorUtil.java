package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class ColorUtil {
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
