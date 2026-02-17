package org.phenopackets.phenopackettools.validator.core;

import org.monarchinitiative.phenol.io.MinimalOntologyLoader;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class TestData {

    public static final MinimalOntology HPO;

    static {
        try {
            HPO = MinimalOntologyLoader.loadOntology(new GZIPInputStream(
                    new BufferedInputStream(
                            Objects.requireNonNull(TestData.class.getResourceAsStream("hp.v2026-01-08.json.gz"))
                    )
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
