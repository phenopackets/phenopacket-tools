package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;

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

    public static Timestamp seconds(long seconds) {
        return Timestamp.newBuilder().setSeconds(seconds).build();
    }

    public static Timestamp now() {
        return TimestampBuilder.seconds(System.currentTimeMillis());
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

    /**
     * Parse a google protobuf timestamp object from a string in RFC 3339 format (e.g., 2019-10-12T07:20:50.52Z)
     * @param tstamp RFC 3339 string
     * @return corresponding protobuf timestamp object
     */
    public static Timestamp fromRFC3339(String tstamp) {
        try {
            return Timestamps.parse(tstamp);
        } catch (Exception e) {
            throw new PhenotoolsRuntimeException("Invalid time string: \"" + tstamp + "\"");
        }
    }

    /**
     * Accepts strings like 2021-10-01T18:58:43Z (also valid RFC3339) and simple Strings
     * like 2021-10-01 (valid ISO 8601 but not RFC3339). Here we assume that the time of day is
     * zero seconds and pass that on to the {@link #fromRFC3339(String)} to get a time stamp
     * @param time a string such as 2021-10-01T18:58:43Z or 2021-10-01
     * @return corresponding protobuf Timestamp object
     */
    public static Timestamp fromISO8601(String time) {
        //2021-10-01T18:58:43Z is valid
        //2021-10-01 is also valid
        // the first is also valid RFC3339
        if (time.contains("T") && time.endsWith("Z")) {
            return fromRFC3339(time);
        } else {
            time += "T00:00:00Z";
            return fromRFC3339(time);
        }
    }



}
