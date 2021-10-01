package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimestampBuilder {

    private final Timestamp.Builder builder;

    public TimestampBuilder(long seconds) {
        builder = Timestamp.newBuilder().setSeconds(seconds);
    }

    public Timestamp build() {
        return builder.build();
    }

    public static Timestamp create(long seconds) {
        return new TimestampBuilder(seconds).build();
    }

    public static Timestamp now() {
        return TimestampBuilder.create(System.currentTimeMillis());
    }

    public static Timestamp fromYMD(int y, int m, int d) {
        LocalDateTime timeNow = LocalDate.of(y, m, d).atTime(0, 0);
        return Timestamp.newBuilder()
                .setSeconds(timeNow.toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    public static Timestamp fromYMD(int y, int m, int d, int h, int min) {
        LocalDateTime timeNow = LocalDate.of(y, m, d).atTime(h, min);
        return Timestamp.newBuilder()
                .setSeconds(timeNow.toEpochSecond(ZoneOffset.UTC))
                .build();
    }





}
