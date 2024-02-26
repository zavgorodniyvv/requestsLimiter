package com.example.limiter.service;

import java.time.*;

public class TimeUtils {
    public static LocalTime getCurrentTimeUTC() {
        return getCurrentDateTimeUTC().toLocalTime();
    }

    public static LocalDateTime getCurrentDateTimeUTC() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTimeInCurrentZone = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeInUtc = zonedDateTimeInCurrentZone.withZoneSameInstant(ZoneId.of("UTC"));
        return zonedDateTimeInUtc.toLocalDateTime();
    }

    public static LocalTime getTimeInUTC(LocalTime currentLocalTime) {
        OffsetTime offsetTimeInCurrentZone = currentLocalTime.atOffset(ZoneId.systemDefault().getRules().getOffset(currentLocalTime.atDate(LocalDate.now())));
        OffsetTime offsetTimeInUtc = offsetTimeInCurrentZone.withOffsetSameInstant(ZoneOffset.UTC);
        return offsetTimeInUtc.toLocalTime();
    }

}
