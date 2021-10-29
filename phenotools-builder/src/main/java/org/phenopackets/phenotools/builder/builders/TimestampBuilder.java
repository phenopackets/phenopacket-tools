package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;

import java.time.*;

public class TimestampBuilder {

    private final Timestamp.Builder builder;

    public TimestampBuilder(long seconds) {
        builder = Timestamp.newBuilder().setSeconds(seconds);
    }

    public Timestamp build() {
        return builder.build();
    }

    public static Timestamp seconds(long seconds) {
        return Timestamp.newBuilder().setSeconds(seconds).build();
    }

    public static Timestamp now() {
        Instant instant = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    public static Timestamp timestamp(int y, int m, int d) {
        LocalDateTime timeNow = LocalDate.of(y, m, d).atTime(0, 0);
        return Timestamp.newBuilder()
                .setSeconds(timeNow.toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    public static Timestamp timestamp(int y, int m, int d, int h, int min) {
        LocalDateTime timeNow = LocalDate.of(y, m, d).atTime(h, min);
        return Timestamp.newBuilder()
                .setSeconds(timeNow.toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    /**
     * Accepts strings with the ISO8601 date format e.g. 2021-10-01 and returns a timestamp for midnight UTC of that day.
     * @param date a string such as 2021-10-01
     * @return corresponding protobuf Timestamp object
     */
    public static Timestamp fromISO8601LocalDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return Timestamp.newBuilder()
                .setSeconds(localDate.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC))
                .build();
    }

    /**
     * Accepts strings like 2021-10-01T18:58:43Z (also valid RFC3339)
     * @param time a string such as 2021-10-01T18:58:43Z
     * @return corresponding protobuf Timestamp object
     */
    public static Timestamp fromISO8601(String time) {
        Instant instant = Instant.parse(time);
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
