package org.phenopackets.phenopackettools.validator.jsonschema;

import java.nio.file.Path;

public class TestData {

    public static final Path TEST_BASE_DIR = Path.of("src/test/resources/org/phenopackets/phenopackettools/validator/jsonschema");

    /**
     * A path to an example phenopacket that, despite being medically invalid/nonsense, is complete from the testing
     * point of view.
     * <p>
     * The phenopacket has been doctored to:
     * <ul>
     *     <li>contain valid value in all required fields,</li>
     *     <li>contain all types of time elements within the first 6 phenotypic features.</li>
     * </ul>
     */
    public static final Path BETHLEM_MYOPATHY_PHENOPACKET_JSON = TEST_BASE_DIR.resolve("bethlem-myopathy.json");

}
