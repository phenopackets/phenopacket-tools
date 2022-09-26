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


    /**
     * A path to an example family that, despite being medically invalid/nonsense, is complete from the testing
     * point of view.
     * <p>
     * The family has been doctored to contain:
     * <ul>
     *     <li>valid id,</li>
     *     <li>proband (content of {@link #BETHLEM_MYOPATHY_PHENOPACKET_JSON}),</li>
     *     <li>pedigree representing trio with unaffected parents, and</li>
     *     <li>metadata</li>
     * </ul>
     */
    public static final Path EXAMPLE_FAMILY_JSON = TEST_BASE_DIR.resolve("example-family.json");

    /**
     * A path to an example cohort that, despite being medically invalid/nonsense, is complete from the testing
     * point of view.
     * <p>
     * The cohort has been doctored to contain:
     * <ul>
     *     <li>valid id,</li>
     *     <li>proband (content of {@link #BETHLEM_MYOPATHY_PHENOPACKET_JSON}),</li>
     *     <li>pedigree representing trio with unaffected parents, and</li>
     *     <li>metadata</li>
     * </ul>
     */
    public static final Path EXAMPLE_COHORT_JSON = TEST_BASE_DIR.resolve("example-cohort.json");

}
