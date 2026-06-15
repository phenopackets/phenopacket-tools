package org.phenopackets.phenopackettools.validator.core;

import org.monarchinitiative.phenol.io.MinimalOntologyLoader;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;

import java.io.File;
import java.net.URL;
import java.util.Objects;


public class TestData {

    private static final URL HP_MODULE_URL = TestData.class.getResource("hp.module.json");
    public static final MinimalOntology HPO = MinimalOntologyLoader.loadOntology(new File(Objects.requireNonNull(HP_MODULE_URL.getPath())));

}
