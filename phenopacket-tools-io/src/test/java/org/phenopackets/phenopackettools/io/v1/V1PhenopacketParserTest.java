package org.phenopackets.phenopackettools.io.v1;

import com.google.protobuf.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.phenopackettools.io.PhenopacketParser;
import org.phenopackets.phenopackettools.io.TestBase;
import org.phenopackets.phenopackettools.core.PhenopacketElement;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.schema.v1.Cohort;
import org.phenopackets.schema.v1.Family;
import org.phenopackets.schema.v1.Phenopacket;

import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class V1PhenopacketParserTest {

    private static final Path BASE = TestBase.BASE_DIR.resolve("v1");

    private PhenopacketParser parser;

    @BeforeEach
    public void setUp() {
        parser = V1PhenopacketParser.INSTANCE;
    }

    @ParameterizedTest
    @CsvSource({
            "PROTOBUF,     PHENOPACKET,     phenopacket.pb",
            "PROTOBUF,          FAMILY,     family.pb",
            "PROTOBUF,          COHORT,     cohort.pb",
            "    JSON,     PHENOPACKET,     phenopacket.json",
            "    JSON,          FAMILY,     family.json",
            "    JSON,          COHORT,     cohort.json",
            "    YAML,     PHENOPACKET,     phenopacket.yaml",
            "    YAML,          FAMILY,     family.yaml",
            "    YAML,          COHORT,     cohort.yaml",
    })
    public void weGetExpectedClassForGivenFormatAndElement(PhenopacketFormat format,
                                                           PhenopacketElement element,
                                                           String fileName) throws Exception {
        Message message = parser.parse(format, element, BASE.resolve(fileName));

        assertThat(message, is(instanceOf(getClassForPhenopacketElement(element))));
    }

    private static Class<?> getClassForPhenopacketElement(PhenopacketElement element) {
        return switch (element) {
            case PHENOPACKET -> Phenopacket.class;
            case FAMILY -> Family.class;
            case COHORT -> Cohort.class;
        };
    }
}
