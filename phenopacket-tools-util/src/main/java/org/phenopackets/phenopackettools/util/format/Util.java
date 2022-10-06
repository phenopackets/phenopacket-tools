package org.phenopackets.phenopackettools.util.format;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {

    private static final Pattern YAML_HEADER = Pattern.compile("((phenopacket)|(family)|(cohort)):");

    private Util() {
        // static utility class
    }

    static boolean looksLikeJson(byte[] payload) {
        String head = new String(payload, 0, FormatSniffer.BUFFER_SIZE);
        return looksLikeJson(head);
    }

    static boolean looksLikeJson(String head) {
        return head.replace("\\W+", "").startsWith("{");
    }

    static boolean looksLikeYaml(byte[] payload) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(payload)))) {
            String line = reader.readLine();
            Matcher matcher = YAML_HEADER.matcher(line);
            return matcher.matches();
        } catch (IOException e) {
            return false;
        }
    }

}
