package org.phenopackets.phenotools.builder.builders;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.schema.v2.core.ExternalReference;
import org.phenopackets.schema.v2.core.OntologyClass;

import static org.phenopackets.phenotools.builder.builders.OntologyClassBuilder.ontologyClass;


/**
 * This class contains some commonly used static functions and constants for various common tasks
 * @author Peter N Robinson
 */
public class PhenoBuilder {

    public final static OntologyClass HOMO_SAPIENS = ontologyClass("NCBI:txid9606", "Homo sapiens");
    public final static String SCHEMA_VERSION = "2.0";

    public static ExternalReference externalReference(String id, String description) {
        return ExternalReference.newBuilder().setId(id).setDescription(description).build();
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
     * @param time
     * @return
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
