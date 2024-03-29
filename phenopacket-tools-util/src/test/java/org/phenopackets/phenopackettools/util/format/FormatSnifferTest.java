package org.phenopackets.phenopackettools.util.format;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class FormatSnifferTest {

    private static final Path BASE_DIR = TestResources.BASE_DIR.resolve("format");

    @ParameterizedTest
    @CsvSource({
            "comprehensive-phenopacket.pb,    PROTOBUF",
            "covid.json,                      JSON",
            "covid.yml,                       YAML",

            "comprehensive-family.pb,         PROTOBUF",
            "family.v2.json,                  JSON",
            "family.v2.yml,                   YAML",

            "comprehensive-cohort.pb,         PROTOBUF",
            "cohort.v2.json,                  JSON",
            "cohort.v2.yml,                   YAML",
    })
    public void sniff(String fileName, PhenopacketFormat expected) throws Exception {
        byte[] payload = readAllBytes(fileName);
        PhenopacketFormat format;
        format = FormatSniffer.sniff(payload);

        assertThat(format, equalTo(expected));
    }

    private static byte[] readAllBytes(String fileName) throws IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(BASE_DIR.resolve(fileName)))) {
            return is.readAllBytes();
        }
    }
}