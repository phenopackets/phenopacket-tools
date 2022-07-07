package org.phenopackets.phenopackettools.validator.core;

public record OntologyError(String name, String message) implements ValidationErrorType{


   //

    public static OntologyError invalidOntology(String message) {
        return new OntologyError("invalid ontology", message);
    }

}
