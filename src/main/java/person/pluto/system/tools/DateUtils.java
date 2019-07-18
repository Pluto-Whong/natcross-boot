package person.pluto.system.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * <p>
 * 日期工具集
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 11:17:15
 */
public final class DateUtils {
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter DATETIMEMILLIS = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMddHHmmss").appendValue(ChronoField.MILLI_OF_SECOND, 3).toFormatter();

    public static final DateTimeFormatter DATETIME_FORMAT_Add = DateTimeFormatter.ofPattern("yyyy-M-d+HH:mm:ss");
    public static final DateTimeFormatter DATETIME_MINUTE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATE_CHINA = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:06:46
     * @param value
     * @return
     */
    public static LocalDateTime getDateTimeFormatByString(String value) {
        return LocalDateTime.parse(value, DATETIME_FORMAT);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:08:22
     * @param dateTime
     * @return
     */
    public static String getDateTimeFormatToString(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMAT);
    }

    /**
     * yyyyMMddHHmmss
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:07:40
     * @param value
     * @return
     */
    public static LocalDateTime getDateTimeByString(String value) {
        return LocalDateTime.parse(value, DATETIME);
    }

    /**
     * yyyyMMddHHmmssSSS
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:10:03
     * @param dateTime
     * @return
     */
    public static String getDateTimeMillisToString(LocalDateTime dateTime) {
        return dateTime.format(DATETIMEMILLIS);
    }

    /**
     * yyyyMMddHHmmssSSS
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:07:40
     * @param value
     * @return
     */
    public static LocalDateTime getDateTimeMillisByString(String value) {
        return LocalDateTime.parse(value, DATETIMEMILLIS);
    }

    /**
     * yyyyMMddHHmmss
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:10:03
     * @param dateTime
     * @return
     */
    public static String getDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DATETIME);
    }

    /**
     * yyyy-M-d+HH:mm:ss
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:06:55
     * @param value
     * @return
     */
    public static LocalDateTime getDateTimeFormatAddByStringAdd(String value) {
        return LocalDateTime.parse(value, DATETIME_FORMAT_Add);
    }

    /**
     * yyyy-M-d+HH:mm:ss
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:10:03
     * @param dateTime
     * @return
     */
    public static String getDateTimeFormatAddToString(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMAT_Add);
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:08:02
     * @param value
     * @return
     */
    public static LocalDateTime getDateMinuteByString(String value) {
        return LocalDateTime.parse(value, DATETIME_MINUTE);
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:10:38
     * @param dateTime
     * @return
     */
    public static String getDateMinuteToString(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_MINUTE);
    }

    /**
     * yyyy年MM月dd日
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:08:02
     * @param value
     * @return
     */
    public static LocalDateTime getDateChinaByString(String value) {
        return LocalDateTime.parse(value, DATE_CHINA);
    }

    /**
     * yyyy年MM月dd日
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:10:50
     * @param dateTime
     * @return
     */
    public static String getDateChinaToString(LocalDateTime dateTime) {
        return dateTime.format(DATE_CHINA);
    }

    /**
     * yyyyMMdd
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:08:02
     * @param value
     * @return
     */
    public static LocalDateTime getDateFormatByString(String value) {
        return LocalDateTime.parse(value, DATE_FORMAT);
    }

    /**
     * yyyyMMdd
     *
     * @author wangmin1994@qq.com
     * @since 2019-06-12 19:12:54
     * @param dateTime
     * @return
     */
    public static String getDateFormatToString(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMAT);
    }
}
