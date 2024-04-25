package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColorUtil {

    public final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");

    public String color(String text) {

        if(text == null || text.isEmpty()) {
            return text;
        }

        Matcher matcher = HEX_COLOR_PATTERN.matcher(text);

        while (matcher.find()) {
            final String color = text.substring(matcher.start() + 1, matcher.end());
            text = text.replace(color, ChatColor.of(color) + "");
            matcher = HEX_COLOR_PATTERN.matcher(text);
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
