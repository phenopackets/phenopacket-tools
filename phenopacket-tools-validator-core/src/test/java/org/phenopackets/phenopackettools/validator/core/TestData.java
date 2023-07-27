package org.phenopackets.phenopackettools.validator.core;

import org.monarchinitiative.phenol.io.MinimalOntologyLoader;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;

import java.nio.file.Path;

public class TestData {

    public static final Path TEST_BASE_DIR = Path.of("src/test/resources/org/phenopackets/phenopackettools/validator/core");
    private static final Path HPO_MODULE_PATH = TEST_BASE_DIR.resolve("hp.module.json");

    public static final MinimalOntology HPO = MinimalOntologyLoader.loadOntology(HPO_MODULE_PATH.toFile());

}
