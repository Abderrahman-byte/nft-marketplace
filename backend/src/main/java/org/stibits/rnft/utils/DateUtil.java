package org.stibits.rnft.utils;

import java.util.Calendar;

public class DateUtil {
    public static Calendar getMonthInterval (int count) {
        return DateUtil.getInterval(Calendar.MONTH, count);
    }

    public static Calendar getDayInterval (int count) {
        return DateUtil.getInterval(Calendar.DATE, count);
    }

    public static Calendar getWeekInterval (int count) {
        return DateUtil.getInterval(Calendar.WEEK_OF_YEAR, count);
    }

    private static Calendar getInterval (int field, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, count * -1);

        return calendar;
    }
}
