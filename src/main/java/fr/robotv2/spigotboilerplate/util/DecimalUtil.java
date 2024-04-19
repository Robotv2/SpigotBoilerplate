package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class DecimalUtil {

    private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");

    public String format(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public String percentage(double currentValue, double maximumValue) {
        return percentage(currentValue, maximumValue, 100);
    }

    public String percentage(double currentValue, double maximumValue, double scale) {
        return format(Math.min(currentValue * scale / maximumValue, scale));
    }
}
