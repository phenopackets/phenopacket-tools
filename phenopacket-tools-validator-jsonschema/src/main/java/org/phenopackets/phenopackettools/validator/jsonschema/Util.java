package org.phenopackets.phenopackettools.validator.jsonschema;

class Util {

    private Util() {
        // static utility class
    }

    static boolean looksLikeJson(byte[] payload) {
        byte[] first32bytes = new byte[32];
        System.arraycopy(payload, 0, first32bytes, 0, first32bytes.length);
        return looksLikeJson(new String(first32bytes));
    }

    static boolean looksLikeJson(String head) {
        return head.replace("\\W+", "").startsWith("{");
    }
}
