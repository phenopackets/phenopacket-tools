package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.phenotools.builder.builders.constants.Severity;
import org.phenopackets.schema.v2.core.Evidence;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;
import org.phenopackets.schema.v2.core.TimeElement;

import java.util.List;

/**
 * This has convenience methods for building PhenotypicFeature messages with some
 * commonly used options.
 *
 * @author Peter N Robinson
 */
public class PhenotypicFeatureBuilder {

    private final PhenotypicFeature.Builder builder;

    private PhenotypicFeatureBuilder(OntologyClass feature) {
        builder = PhenotypicFeature.newBuilder().setType(feature);
    }

    public static PhenotypicFeature phenotypicFeature(OntologyClass feature) {
        return PhenotypicFeature.newBuilder().setType(feature).build();
    }

    public static PhenotypicFeature phenotypicFeature(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return phenotypicFeature(ontologyClass);
    }

    public static PhenotypicFeatureBuilder builder(OntologyClass feature) {
        return new PhenotypicFeatureBuilder(feature);
    }

    public static PhenotypicFeatureBuilder builder(String id, String label) {
        OntologyClass ontologyClass = OntologyClassBuilder.ontologyClass(id, label);
        return builder(ontologyClass);
    }

    public PhenotypicFeatureBuilder onset(TimeElement time) {
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder congenitalOnset() {
        TimeElement time = TimeElements.congenitalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder embryonalOnset() {
        TimeElement time = TimeElements.embryonalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder fetalOnset() {
        TimeElement time = TimeElements.fetalOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder infantileOnset() {
        TimeElement time = TimeElements.infantileOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder childhoodOnset() {
        TimeElement time = TimeElements.childhoodOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder adultOnset() {
        TimeElement time = TimeElements.adultOnset();
        builder.setOnset(time);
        return this;
    }

    public PhenotypicFeatureBuilder severity(String id, String label) {
        OntologyClass severity = OntologyClassBuilder.ontologyClass(id, label);
        builder.setSeverity(severity);
        return this;
    }

    public PhenotypicFeatureBuilder severe() {
        builder.setSeverity(Severity.severe());
        return this;
    }

    public PhenotypicFeatureBuilder excluded() {
        builder.setExcluded(true);
        return this;
    }

    public PhenotypicFeatureBuilder evidence(Evidence evidence) {
        builder.addEvidence(evidence);
        return this;
    }

    public PhenotypicFeatureBuilder allEvidence(List<Evidence> evidenceList) {
        builder.addAllEvidence(evidenceList);
        return this;
    }

    public PhenotypicFeatureBuilder modifier(OntologyClass clz) {
        builder.addModifiers(clz);
        return this;
    }

    public PhenotypicFeatureBuilder allModifiers(List<OntologyClass> modifiers) {
        builder.addAllModifiers(modifiers);
        return this;
    }

    public PhenotypicFeature build() {
        return builder.build();
    }

}
