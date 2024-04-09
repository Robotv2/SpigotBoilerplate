package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class DecimalUtil {

    private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");

    public String format(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public String pourcentage(double value, double max) {
        return format(Math.min(value * 100 / max, 100));
    }
}
