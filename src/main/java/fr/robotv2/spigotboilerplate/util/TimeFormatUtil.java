package fr.robotv2.spigotboilerplate.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class TimeFormatUtil {

    public String formatTimeFull(long millis) {
        return formatTimeFull(millis, "%d days, %02d hours, %02d minutes, %02d seconds");
    }

    public String formatTimeFull(long millis, String format) {

        final Duration duration = Duration.ofMillis(millis);

        final long days = duration.toDays();
        final long hours = duration.toHoursPart();
        final long minutes = duration.toMinutesPart();
        final long seconds = duration.toSecondsPart();

        return String.format(format, days, hours, minutes, seconds);
    }

    public String formatTimeCompact(long millis) {
        final Duration duration = Duration.ofMillis(millis);

        final long days = duration.toDays();
        final long hours = duration.toHoursPart();
        final long minutes = duration.toMinutesPart();
        final long seconds = duration.toSecondsPart();

        if (days > 0) {
            return String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public String formatTimeMinimal(long millis) {
        final Duration duration = Duration.ofMillis(millis);

        final long days = duration.toDays();
        final long hours = duration.toHoursPart();
        final long minutes = duration.toMinutesPart();
        final long seconds = duration.toSecondsPart();

        if (days > 0) {
            return String.format("%d days %d hr %d min %d sec", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%d hr %d min %d sec", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d min %d sec", minutes, seconds);
        } else {
            return String.format("%d sec", seconds);
        }
    }
}
