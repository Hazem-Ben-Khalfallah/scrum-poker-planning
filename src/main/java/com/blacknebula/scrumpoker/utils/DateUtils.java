package com.blacknebula.scrumpoker.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Get a current time
     *
     * @return an instance of Date representing the current date time on the
     * system timezone
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    /**
     * add days to a date
     *
     * @param date an instance of a date
     * @param days number of days to set
     * @return an instance of a date with the days added
     */
    public static Date addDays(Date date, int days) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * add seconds to a date
     *
     * @param date    an instance of a date
     * @param seconds number of seconds to set
     * @return an instance of a date with the seconds added
     */
    public static Date addSeconds(Date date, int seconds) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    public static String format(String pattern) {
        final DateFormat df = new SimpleDateFormat(pattern);
        return df.format(Calendar.getInstance().getTime());
    }

    public static Date getDate(int year, int month, int day) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day, int hours, int minutes, int seconds) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day, int hours, int minutes, int seconds, String timeZone) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        return cal.getTime();
    }

}
