package de.liga.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:43:41
 */
public class CalendarUtils {
    /**
     * @return a time with the given hour:minute (seconds = 0)
     */
    public static Time createTime(int hour, int minute) {
        Calendar gc = Calendar.getInstance();
        gc.clear();
        gc.set(0, 0, 0, hour, minute, 0);
        return new Time(gc.getTimeInMillis());
    }

    public static int getYear(Date d) {
        Calendar gc = Calendar.getInstance();
        gc.clear();
        gc.setTime(d);
        return gc.get(Calendar.YEAR);
    }

    public static int getMonth(Date d) {
        Calendar gc = Calendar.getInstance();
        gc.clear();
        gc.setTime(d);
        return gc.get(Calendar.MONTH) + 1;
    }

    public static String timeToString(Time time) {
        if (time == null) return null;
        SimpleDateFormat timeFormHHMM = new SimpleDateFormat("HH:mm");
        timeFormHHMM.setLenient(false);
        return timeFormHHMM.format(time);
    }

    public static Time stringToTime(String time) {
        if (StringUtils.isEmpty(time)) return null;
        SimpleDateFormat timeFormHHMM = new SimpleDateFormat("HH:mm");
        timeFormHHMM.setLenient(false);
        try {
            return new Time(timeFormHHMM.parse(time).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date stringToDate(String date, String format) {
        if (StringUtils.isEmpty(date)) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            return new Date(dateFormat.parse(date).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(java.util.Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * database time formatted string -> Time
     *
     * @param time
     * @return
     */
    public static Time dbstringToTime(String time) {
        if (StringUtils.isEmpty(time)) return null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setLenient(false);
        try {
            return new Time(format.parse(time).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * database timestamp formatted string -> Timestamp
     * e.g. 2009-02-22 14:12:04.468
     *
     * @param timestamp
     * @return
     */
    public static Timestamp dbstringToTimestamp(String timestamp) {
        if (StringUtils.isEmpty(timestamp)) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        format.setLenient(false);
        try {
            return new Timestamp(format.parse(timestamp).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getWeekdayName(int dayOfWeek) {
        DateFormatSymbols symbols = DateFormatSymbols.getInstance();
        return symbols.getWeekdays()[dayOfWeek];
    }

    public static Integer getWeekday(String dayOfWeek) {
        DateFormatSymbols symbols = DateFormatSymbols.getInstance();
        int index = ArrayUtils.indexOf(symbols.getWeekdays(), dayOfWeek);
        return index <= 0 ? null : Integer.valueOf(index);
    }

    public static String[] getWeekdays() {
        DateFormatSymbols symbols = DateFormatSymbols.getInstance();
        return symbols.getWeekdays();
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * konvertiert in Daten der altanwendung
     *
     * @param time
     * @return
     */
    public static int toVfsValue(Time time) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(time);
        int h = gc.get(Calendar.HOUR_OF_DAY);
        int m = gc.get(Calendar.MINUTE);
        return (h * 60 * 60) + (m * 60);
    }
}
