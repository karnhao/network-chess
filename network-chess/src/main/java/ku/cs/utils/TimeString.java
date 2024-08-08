package ku.cs.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeString {
    public static String getCurrentTimeString() {
        return getCurrentTimeString("dd-MM-yyyy HH:mm:ss");
    }

    public static String getCurrentTimeString(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
}
