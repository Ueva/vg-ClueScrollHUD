package io.github.ueva.cluescrollhud.utils;

public class DateTimeUtils {

    public static String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days >= 1) {
            long remHours = hours % 24;
            return String.format("%d Day%s, %d Hour%s", days, days == 1 ? "" : "s", remHours, remHours == 1 ? "" : "s");
        }
        else if (hours >= 1) {
            long remMinutes = minutes % 60;
            return String.format(
                    "%d Hour%s, %d Minute%s",
                    hours,
                    hours == 1 ? "" : "s",
                    remMinutes,
                    remMinutes == 1 ? "" : "s"
            );
        }
        else if (minutes >= 1) {
            long remSeconds = seconds % 60;
            return String.format(
                    "%d Minute%s, %d Second%s",
                    minutes,
                    minutes == 1 ? "" : "s",
                    remSeconds,
                    remSeconds == 1 ? "" : "s"
            );
        }
        else {
            return String.format("%d Second%s", seconds, seconds == 1 ? "" : "s");
        }
    }

}
