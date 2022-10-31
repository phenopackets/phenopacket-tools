package org.phenopackets.phenopackettools.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.phenopackets.phenopackettools.core.PhenopacketSchemaVersion;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PhenopacketParserFactoryImplTest {

    private PhenopacketParserFactoryImpl parserFactory;

    @BeforeEach
    public void setUp() {
        parserFactory = PhenopacketParserFactoryImpl.INSTANCE;
    }

    @ParameterizedTest
    @CsvSource({
            "V1",
            "V2"
    })
    public void weHaveAParserForAllSchemaVersions(PhenopacketSchemaVersion version) {
        PhenopacketParser parser = parserFactory.forFormat(version);
        assertThat(parser, is(notNullValue()));
    }

}