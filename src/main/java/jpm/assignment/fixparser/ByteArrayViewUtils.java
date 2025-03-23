package jpm.assignment.fixparser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public enum ByteArrayViewUtils {
    INSTANCE;

    public int parseInt(ByteArrayView view) {
        int num  = 0;
        int sign = -1;
        final int len  = view.length();
        final char ch  = view.charAt(0);
        if (ch == '-')
            sign = 1;
        else
            num = '0' - ch;

        int i = 1;
        while ( i < len )
            num = num * 10 + '0' - view.charAt(i++);

        return sign * num;
    }

    public ZonedDateTime parseTime(ByteArrayView view) {
        int year = (view.charAt(0) - '0') * 1000 + (view.charAt(1) - '0') * 100 +
                (view.charAt(2) - '0') * 10 + (view.charAt(3) - '0');

        int month = (view.charAt(5) - '0') * 10 + (view.charAt(6) - '0');

        int day = (view.charAt(8) - '0') * 10 + (view.charAt(9) - '0');

        int hour = (view.charAt(11) - '0') * 10 + (view.charAt(12) - '0');

        int minute = (view.charAt(13) - '0') * 10 + (view.charAt(14) - '0');

        int second = (view.charAt(15) - '0') * 10 + (view.charAt(16) - '0');

        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return localDateTime.atZone(ZoneOffset.UTC); // UTC is
    }
}
