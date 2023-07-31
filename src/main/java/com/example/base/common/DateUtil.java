package com.example.base.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

@Slf4j
public class DateUtil {
    public static final String YYYY = "yyyy";
    public static final String DD_MM = "dd/MM";
    public static final String DDMMYYYY = "ddMMyyyy";
    public static final String MMYYYY = "MMyyyy";
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String YYYY_MM_DD = "yyyy/MM/dd";
    public static final String YYYY_MM_DD_2 = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYMMDD = "yyMMdd";
    public static final String DDMMYY = "ddMMyy";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYMMDDHHMMSS = "yyMMddHHmmss";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM_SS_Z = "dd/MM/yyyy HH:mm:ss z";
    public static final String YYYY_MM_DD_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DD_MM_YYYY_2 = "dd-MM-yyyy";
    public static final String UTC_NATIONAL = "UTC";
    public static final String UTC_VIETNAM = "UTC+7";

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
        return df.format(date);
    }

    /**
     * convert String to Date      *      * @param dateStr      * @author hcmhth
     */
    public static Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime getDateTimeNowUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime getDateTimeNowUTC_VN() {
        OffsetDateTime offsetDateTime = Instant.now().atOffset(ZoneOffset.UTC);
        ZonedDateTime zonedDateTimeVN = offsetDateTime.atZoneSameInstant(ZoneId.of(UTC_VIETNAM));
        return zonedDateTimeVN.toLocalDateTime();
    }

    public static Date toDateUTC(LocalDateTime localDateTime) {
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    public static LocalDateTime parseDateTimeUTC(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            Instant instant = Instant.parse(dateStr);
            LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime parseDateTimeUTC_VN(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            Instant instant = Instant.parse(dateStr);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(UTC_VIETNAM));
            return zonedDateTime.toLocalDateTime();
        } catch (Exception e) {
            log.debug("parseDateTimeUTC_VN encounter an error {} with {}", e.getMessage(), dateStr);
        }
        return null;
    }

    public static Date parseDateUTC_VN(String dateStr) {
        try {
            LocalDateTime localDateTime = parseDateTimeUTC_VN(dateStr);
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            log.debug("parseDateUTC_VN encounter an error {} with {}", e.getMessage(), dateStr);
        }
        return null;
    }

    public static Date parseDateTime(Object obj) {
        try {
            if (obj == null) return null;
            String dateStr = String.valueOf(obj);
            SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS);
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseToString(Date date, String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}