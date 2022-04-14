package org.phenopackets.phenopackettools.validator.core;


public enum ValidationAspect {

    // This enum is supposed to be used instead of the ErrorType
    // TODO - elaborate the categories

    // general syntax inconsistency, e.g. bad JSON format
    GENERAL,

    // e.g. missing at least one phenotypic feature, age, etc.
    MISSING_PROPERTY,

    // e.g. invalid ontology used to represent an info
    INVALID_VALUE,

}
