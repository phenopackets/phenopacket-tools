package org.phenopackets.phenotools.builder.builders;

import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.ReferenceRange;

/**
 * Simple wrapper for a ReferenceRange
 *  unit:
 *   id: "UO:0000316"
 *   label: "cells per microliter"
 *  low: 150000.0
 *  high: 450000.0
 *  @author  Peter N Robinson
 */
public class ReferenceRangeBuilder {

    private ReferenceRangeBuilder() {
    }

    public static ReferenceRange referenceRange(OntologyClass unit, double low, double high) {
        return ReferenceRange.newBuilder().setUnit(unit).setLow(low).setHigh(high).build();
    }

    public static ReferenceRange referenceRange(String id, String label, double low, double high) {
        OntologyClass unit = OntologyClassBuilder.ontologyClass(id, label);
        return referenceRange(unit, low, high);
    }

}
