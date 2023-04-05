package org.phenopackets.phenopackettools.util.format;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Util {

    /*
     Field names of the top-level elements of Phenopacket Schemas v1 and v2.
     The order of fields in YAML format is not prescribed, hence any of these fields can be present at the first line.
     YAML uses camelCase for field naming.
     */
    private static final String[] FIELD_NAMES = {
            // Phenopacket
            "id", "subject", "phenotypicFeatures", "measurements", "biosamples",
            "interpretations", "diseases", "medicalActions", "files", "metaData",
            // Family (id, files and metaData are already included)
            "proband", "relatives", "pedigree",
            // Cohort (id, files, and metaData are already included)
            "description", "members",
            // v1 members
            "genes", "variants", "resolutionStatus", "diagnosis", "phenopacket", "family"
    };

    private static final String FIELD_GROUPS = Arrays.stream(FIELD_NAMES)
            .map("(%s)"::formatted)
            .collect(Collectors.joining("|", "^(", ")"));

    /*
    A pattern for a YAML field id followed by an optional value.
    Examples:
    - `subject:`
    - `id: "family-phenopacket"``
    */
    private static final Pattern YAML_HEADER = Pattern.compile(FIELD_GROUPS);
    // Any top-level message of the Phenopacket v1 or v2 schemas is a JSON object (not an array), hence it must start
    // with a `{` character, optionally prepended with white-space.
    private static final Pattern JSON_HEAD = Pattern.compile("^\\s*\\{");

    private Util() {
        // static utility class
    }

    static boolean looksLikeJson(byte[] payload) {
        String head = new String(payload, StandardCharsets.UTF_8);
        return looksLikeJson(head);
    }

    static boolean looksLikeJson(String head) {
        Matcher matcher = JSON_HEAD.matcher(head);
        return matcher.find();
    }

    static boolean looksLikeYaml(byte[] payload) {
        String string = new String(payload, StandardCharsets.UTF_8);
        Matcher matcher = YAML_HEADER.matcher(string);
        return matcher.find();
    }

    static byte[] getAtMostNFirstBytesAndReset(InputStream input, int nBytes) throws SniffException, IOException {
        if (input.markSupported()) {
            byte[] buffer = new byte[nBytes];
            input.mark(nBytes);
            input.read(buffer);
            input.reset();
            return buffer;
        } else
            throw new SniffException("The provided InputStream does not support `mark()`");

    }
}
