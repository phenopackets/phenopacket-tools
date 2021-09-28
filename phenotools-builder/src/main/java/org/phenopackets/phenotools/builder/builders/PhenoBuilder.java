package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


/**
 * This class contains some commonly used static functions and constants for various common tasks
 * @author Peter N Robinson
 */
public class PhenoBuilder {

    public final static OntologyClass HOMO_SAPIENS = ontologyClass("NCBI:txid9606", "Homo sapiens");

    public static OntologyClass ontologyClass(String termid, String label) {
        return OntologyClass.newBuilder()
                .setId(termid)
                .setLabel(label)
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
            LocalDateTime timeNow = LocalDateTime.now();
            return Timestamp.newBuilder()
                    .setSeconds(timeNow.toEpochSecond(ZoneOffset.UTC))
                    .build();
        }
    }
}
