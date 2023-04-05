package org.phenopackets.phenopackettools.util.format;

import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Make an educated guess regarding which top-level element of Phenopacket schema is represented in the provided
 * {@code byte[]} or {@link  InputStream}.
 */
public class ElementSniffer {

    // Top-level fields unique to a phenopacket, both v1 and v2. If we see this field, the content must be a phenopacket.
    private static final List<String> PHENOPACKET_FIELDS = List.of(
            "subject", "phenotypicFeatures", "measurements", "interpretations", "medicalActions",
            "biosamples", "genes", "variants", "diseases"
    );

    // Top-level fields unique to a family, both v1 and v2. If we see this field, the content must be a family.
    private static final List<String> FAMILY_FIELDS = List.of(
            "proband", "relatives", "pedigree"
    );

    // Top-level fields UNIQUE to a cohort, both v1 and v2. If we see this field, the content must be a cohort.
    private static final List<String> COHORT_FIELDS = List.of(
            "description", "members"
    );

    // Fields shared by all top-level elements:
    // List.of("id", "htsFiles", "files", "metaData")

    // We can leverage the indention feature of the YAML format to get the top-level field names.
    private static final Pattern YAML_TOP_LEVEL_FIELD = Pattern.compile("^(?<field>\\w+):");

    // Pattern for matching JSON field.
    private static final Pattern JSON_FIELD = Pattern.compile("\"(?<field>\\w+)\"\\s*:");

    /**
     * The number of bytes used for element sniffing.
     */
    private static final int BUFFER_SIZE = 1024;

    private ElementSniffer() {
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} present in given {@code input}.
     *
     * @param input  an {@link InputStream} that supports {@link InputStream#mark(int)}.
     * @param format the {@code payload} format
     * @return the sniffed {@link PhenopacketElement}.
     * @throws IOException    in case an error occurs while reading the {@code input}.
     * @throws SniffException if there are not enough bytes available in the {@code input} of if the {@code input} does not
     *                        support {@link InputStream#mark(int)}.
     * @deprecated in favor of {@link #sniff(InputStream, PhenopacketFormat)}.
     */
    // REMOVE(v1.0.0)
    public static PhenopacketElement sniff(InputStream input,
                                           PhenopacketSchemaVersion schemaVersion,
                                           PhenopacketFormat format) throws IOException, SniffException {
        return sniff(input, format);
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} present in given {@code input}.
     *
     * @param input  an {@link InputStream} that supports {@link InputStream#mark(int)}.
     * @param format the {@code payload} format
     * @return the sniffed {@link PhenopacketElement}.
     * @throws IOException    in case an error occurs while reading the {@code input}.
     * @throws SniffException if there are not enough bytes available in the {@code input} of if the {@code input} does not
     *                        support {@link InputStream#mark(int)}.
     */
    public static PhenopacketElement sniff(InputStream input,
                                           PhenopacketFormat format) throws IOException, SniffException {
        return sniff(Util.getAtMostNFirstBytesAndReset(input, BUFFER_SIZE), format);
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} based on given {@code payload}.
     *
     * @param payload buffer with at least the first {@link #BUFFER_SIZE} bytes of the input.
     * @param format  the {@code payload} format
     * @return the sniffed {@link PhenopacketElement}.
     * @throws ElementSniffException if the payload looks suspicious, e.g. contains a unique phenopacket field
     * and a unique cohort field at the same time
     * @deprecated in favor of {@link #sniff(byte[], PhenopacketFormat)}
     */
    // REMOVE(v1.0.0)
    public static PhenopacketElement sniff(byte[] payload,
                                           PhenopacketSchemaVersion schemaVersion,
                                           PhenopacketFormat format) throws ElementSniffException {
        return sniff(payload, format);
    }

    /**
     * Make an educated guess of {@link PhenopacketElement} based on given {@code payload}.
     *
     * @param payload buffer with at least the first {@link #BUFFER_SIZE} bytes of the input.
     * @param format  the {@code payload} format
     * @return the sniffed {@link PhenopacketElement}.
     * @throws ElementSniffException if the payload looks suspicious, e.g. contains a unique phenopacket field
     * and a unique cohort field at the same time
     */
    public static PhenopacketElement sniff(byte[] payload,
                                           PhenopacketFormat format) throws ElementSniffException {
        return switch (format) {
            case PROTOBUF -> sniffProtobuf(payload);
            case JSON -> sniffJson(payload);
            case YAML -> sniffYaml(payload);
        };
    }

    private static PhenopacketElement sniffProtobuf(byte[] payload) throws ElementSniffException {
        // TODO - implement
        // possibly using https://protobuf.dev/programming-guides/encoding/
        throw new ElementSniffException("Element sniffing from protobuf input is not yet implemented");
    }

    private static PhenopacketElement sniffJson(byte[] payload) throws ElementSniffException {
        List<String> topLevelFields = findTopLevelFieldNames(payload);
        return findPhenopacketElement(topLevelFields);
    }

    private static PhenopacketElement sniffYaml(byte[] payload) throws ElementSniffException {
        String string = new String(payload, StandardCharsets.UTF_8);
        List<String> candidates = Arrays.stream(string.split("\n"))
                .flatMap(line -> {
                    Matcher matcher = YAML_TOP_LEVEL_FIELD.matcher(line);
                    return matcher.find()
                            ? Stream.of(matcher.group("field")) 
                            : Stream.empty();
                })
                .toList();

        return findPhenopacketElement(candidates);
    }

    /**
     * Determine the candidate {@link PhenopacketElement} based on candidate field names.
     * <p>
     * The code attempts to find discriminatory fields; the fields that are unique to specific top-level element.
     *
     * @param candidateFieldNames an iterable with candidate field names.
     * @return the resulting {@link PhenopacketElement}.
     * @throws ElementSniffException if the input is inconsistent (contains fields unique
     * to different top-level elements (e.g. members ({@code Cohort}) and pedigree ({@code Family})))
     * or if sniffing is inconclusive (no discriminatory field was found).
     */
    private static PhenopacketElement findPhenopacketElement(Iterable<String> candidateFieldNames) throws ElementSniffException {
        PhenopacketElement candidate = null;
        String culprit = null; // The field that determined the candidate above.
        for (String field : candidateFieldNames) {
            if (PHENOPACKET_FIELDS.contains(field)) {
                if (candidate == null) {
                    culprit = field;
                    candidate = PhenopacketElement.PHENOPACKET;
                } else {
                    if (candidate != PhenopacketElement.PHENOPACKET) {
                        String message = "Inconsistent field names - %s supports %s but %s supports %s"
                                .formatted(field, PhenopacketElement.PHENOPACKET, culprit, candidate);
                        throw new ElementSniffException(message);
                    }
                }
            } else if (FAMILY_FIELDS.contains(field)) {
                if (candidate == null) {
                    culprit = field;
                    candidate = PhenopacketElement.FAMILY;
                } else {
                    if (candidate != PhenopacketElement.FAMILY) {
                        String message = "Inconsistent field names - %s supports %s but %s supporting %s"
                                .formatted(field, PhenopacketElement.FAMILY, culprit, candidate);
                        throw new ElementSniffException(message);
                    }
                }
            } else if (COHORT_FIELDS.contains(field)) {
                if (candidate == null) {
                    culprit = field;
                    candidate = PhenopacketElement.COHORT;
                } else {
                    if (candidate != PhenopacketElement.COHORT) {
                        String message = "Inconsistent field names - %s supports %s but %s supports %s"
                                .formatted(field, PhenopacketElement.COHORT, culprit, candidate);
                        throw new ElementSniffException(message);
                    }
                }
            }
        }

        if (candidate == null)
            throw new ElementSniffException("Inconclusive element sniffing");

        return candidate;
    }

    private static List<String> findTopLevelFieldNames(byte[] payload) {
        int cursor = 0;
        int objectLevel = 0;
        int arrayLevel = 0;

        List<String> fields = new ArrayList<>();

        String string = new String(payload, StandardCharsets.UTF_8);
        Matcher matcher = JSON_FIELD.matcher(string);


        while (matcher.find()) {
            String field = matcher.group("field");
            for (int i = cursor; i < matcher.start(); i++) {
                switch (string.charAt(i)) {
                    case '{' -> objectLevel++;
                    case '[' -> arrayLevel++;
                    case '}' -> objectLevel--;
                    case ']' -> arrayLevel--;
                }
            }

            if (objectLevel == 1 && arrayLevel == 0)
                // We are only interested in the top-level fields of the JSON document - i.e. where
                // the fields at the 1st level of the document that are not in the array.
                fields.add(field);

            cursor = matcher.end();
        }

        return fields;
    }

}
