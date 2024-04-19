package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class ProgressUtil {
    public String processBar(int amount, int required, int barNumber) {
        final StringBuilder builder = new StringBuilder();

        final int greenBarNumber = amount * barNumber / required;
        final int grayBarNumber = barNumber - greenBarNumber;

        for (int i = 0; i < greenBarNumber; i++) {
            builder.append(ChatColor.GREEN).append("|");
        }

        for (int i = 0; i < grayBarNumber; i++) {
            builder.append(ChatColor.GRAY).append("|");
        }

        return builder.toString();
    }

    public static String processBar(int amount, int required) {
        return processBar(amount, required, 20);
    }
}
