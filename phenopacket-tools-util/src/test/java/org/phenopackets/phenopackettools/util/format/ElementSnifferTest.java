package org.phenopackets.phenopackettools.util.format;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;
import org.phenopackets.phenopackettools.util.TestResources;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ElementSnifferTest {

    private static final Path BASE_DIR = TestResources.BASE_DIR.resolve("format");

    @ParameterizedTest
    @CsvSource({
            "covid.yml,         PHENOPACKET",
            "cohort.v2.yml,     COHORT",
            "family.v2.yml,     FAMILY",
    })
    public void sniffV2Yaml(String fileName, PhenopacketElement expected) throws Exception {
        File phenopacketFile = BASE_DIR.resolve(fileName).toFile();
        try (InputStream is = new BufferedInputStream(new FileInputStream(phenopacketFile))) {
            PhenopacketElement actual = ElementSniffer.sniff(is, PhenopacketSchemaVersion.V2, PhenopacketFormat.YAML);
            assertThat(actual, equalTo(expected));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "covid.json,        PHENOPACKET",
            "cohort.v2.json,    COHORT",
            "family.v2.json,    FAMILY",
    })
    public void sniffV2Json(String fileName, PhenopacketElement expected) throws Exception {
        File phenopacketFile = BASE_DIR.resolve(fileName).toFile();
        try (InputStream is = new BufferedInputStream(new FileInputStream(phenopacketFile))) {
            PhenopacketElement actual = ElementSniffer.sniff(is, PhenopacketSchemaVersion.V2, PhenopacketFormat.JSON);
            assertThat(actual, equalTo(expected));
        }
    }

    @Disabled("Not yet implemented")
    @ParameterizedTest
    @CsvSource({
            "comprehensive-phenopacket.pb,     PHENOPACKET",
            "comprehensive-cohort.pb,          COHORT",
            "comprehensive-family.pb,          FAMILY",
    })
    public void sniffV2Protobuf(String fileName, PhenopacketElement expected) throws Exception {
        File phenopacketFile = BASE_DIR.resolve(fileName).toFile();
        try (InputStream is = new BufferedInputStream(new FileInputStream(phenopacketFile))) {
            PhenopacketElement actual = ElementSniffer.sniff(is, PhenopacketSchemaVersion.V2, PhenopacketFormat.PROTOBUF);
            assertThat(actual, equalTo(expected));
        }
    }
}