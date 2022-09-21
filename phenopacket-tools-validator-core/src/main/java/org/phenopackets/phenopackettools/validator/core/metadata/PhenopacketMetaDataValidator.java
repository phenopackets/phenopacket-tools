package org.phenopackets.phenopackettools.validator.core.metadata;

import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.OntologyClass;

import java.util.*;


public class PhenopacketMetaDataValidator extends BaseMetaDataValidator<PhenopacketOrBuilder>  {



    @Override
    public List<ValidationResult> validate(PhenopacketOrBuilder phenopacket) {
        // get all fields of the Phenopacket
        // filter fields that are instances of OntologyClass
        // cast the objects to OntologyClass

        List<OntologyClass> instances = phenopacket
                .getAllFields()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getName().equals("OntologyClass") )
                .map(Map.Entry::getValue)
                .map(OntologyClass.class::cast)
                .toList();
        // validate that these fields use ontology prefixes that are represented in the MetaData
        // section using a superclass method
        return validateOntologyPrefixes(instances, phenopacket.getMetaData());
    }
}
