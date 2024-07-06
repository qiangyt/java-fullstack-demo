package io.github.qiangyt.common.misc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    /**
     *
     */
    public static final String DAY_FORMAT_TEXT = "yyyy-MM-dd";

    /**
     *
     */
    public static final ThreadLocal<DateFormat> DAY_FORMAT = ThreadLocal
            .withInitial(() -> new SimpleDateFormat(DAY_FORMAT_TEXT));

    /**
     *
     */
    public static final String TIME_FORMAT_TEXT = "HH:mm:ss.SSS";

    /**
     *
     */
    public static final ThreadLocal<DateFormat> TIME_FORMAT = ThreadLocal
            .withInitial(() -> new SimpleDateFormat(TIME_FORMAT_TEXT));

    /**
     *
     */
    public static final String DEFAULT_FORMAT_TEXT = DAY_FORMAT_TEXT + " " + TIME_FORMAT_TEXT;

    /**
     * ISO-8601 Date format
     */
    public static final String DATE_FORMAT_RFC_3339_TEXT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     *
     */
    public static final ThreadLocal<DateFormat> DATE_FORMAT_RFC_3339 = ThreadLocal
            .withInitial(() -> new SimpleDateFormat(DATE_FORMAT_RFC_3339_TEXT));

    /**
     *
     */
    public static final ThreadLocal<DateFormat> DEFAULT_FORMAT = ThreadLocal
            .withInitial(() -> new SimpleDateFormat(DEFAULT_FORMAT_TEXT));

    private DateHelper() {
        // do nothing
    }

    /**
     *
     * @param date
     *
     * @return
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT.get());
    }

    /**
     *
     * @param date
     * @param format
     *
     * @return
     */
    public static String format(Date date, DateFormat format) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    /**
     *
     * @param dateText
     * @param format
     *
     * @return
     *
     * @throws ParseException
     */
    public static Date parse(String dateText, DateFormat format) throws ParseException {
        if (StringHelper.isBlank(dateText)) {
            return null;
        }
        return format.parse(dateText);
    }

    /**
     *
     * @param dateText
     *
     * @return
     *
     * @throws ParseException
     */
    public static Date parse(String dateText) throws ParseException {
        return parse(dateText, DEFAULT_FORMAT.get());
    }

    /**
     *
     * @param dateText
     *
     * @return
     *
     * @throws ParseException
     */
    public static Timestamp parseAsTimestamp(String dateText) throws ParseException {
        Date date = parse(dateText);
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /**
     *
     * @param dateText
     * @param format
     *
     * @return
     *
     * @throws ParseException
     */
    public static Timestamp parseAsTimestamp(String dateText, DateFormat format) throws ParseException {
        var date = parse(dateText, format);
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /**
     *
     * @return
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     *
     * @param date
     * @param format
     *
     * @return
     *
     * @throws ParseException
     */
    public static Date convertDate(Date date, DateFormat format) throws ParseException {
        var dateText = format(date, format);
        return parse(dateText, format);
    }

}
