package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.Evidence;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.ArrayList;
import java.util.List;

import static org.phenopackets.phenotools.builder.builders.PhenoBuilder.ontologyClass;

/**
 * This has convenience methods for building PhenotypicFeature messages with some
 * commonly used options.
 * @author Peter N Robinson
 */
public class PhenotypicFeatureBuilder {

    public static final OntologyClass SEVERE = ontologyClass("HP:0012828", "Severe");

    private PhenotypicFeature.Builder builder;

    public PhenotypicFeatureBuilder(String id, String label) {
        OntologyClass clz = ontologyClass(id,label);
        builder = PhenotypicFeature.newBuilder();
        builder.setType(clz);
    }

    public PhenotypicFeatureBuilder onset(TimeElement time) {
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder congenitalOnset() {
        TimeElement time = TimeElementBuilder.congenitalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder embryonalOnset() {
        TimeElement time = TimeElementBuilder.embryonalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder fetalOnset() {
        TimeElement time = TimeElementBuilder.fetalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder infantileOnset() {
        TimeElement time = TimeElementBuilder.infantileOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder childhoodOnset() {
        TimeElement time = TimeElementBuilder.childhoodOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder adultOnset() {
        TimeElement time = TimeElementBuilder.adultOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder severity(String id, String label) {
        OntologyClass severity = ontologyClass(id, label);
        builder.setSeverity(severity);
        return this;
    }

    public PhenotypicFeatureBuilder severe() {
        builder.setSeverity(SEVERE);
        return this;
    }

    public PhenotypicFeatureBuilder evidence(Evidence evidence) {
        builder.addEvidence(evidence);
        return this;
    }

    public PhenotypicFeatureBuilder addAllEvidence(List<Evidence> evidenceList) {
        builder.addAllEvidence(evidenceList);
        return this;
    }

    public PhenotypicFeatureBuilder modifier(OntologyClass clz) {
        builder.addModifiers(clz);
        return this;
    }

    public PhenotypicFeatureBuilder addAllModifiers(List<OntologyClass> modifiers) {
        builder.addAllModifiers(modifiers);
        return this;
    }

    public PhenotypicFeature build() {
        return builder.build();
    }

    public static PhenotypicFeatureBuilder create(String id, String label) {
        return new PhenotypicFeatureBuilder(id, label);
    }
}
