package org.phenopackets.phenopackettools.validator.core.metadata;

import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.phenopackets.phenopackettools.validator.core.ValidatorInfo.inputValidator;

abstract public class BaseMetaDataValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "MetaDataValidator",
            "MetaDataValidator for Phenopacket, Family, and Cohort",
            "Validate that the MetaData section includes information about all ontologies used");

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }


    protected Set<String> getValidOntologyPrefixes(MetaData metaData) {
        return metaData
                .getResourcesList()
                .stream()
                .map(Resource::getNamespacePrefix)
                .collect(Collectors.toSet());
    }

    protected List<ValidationResult> validateOntologyPrefixes(List<OntologyClass> instances,
                                                              MetaData metaData) {
        List<ValidationResult> results = new ArrayList<>();
        Set<String> validOntologyPrefixes = getValidOntologyPrefixes(metaData);
        for (OntologyClass instance: instances) {
            String id = instance.getId();
            String [] fields =  id.split("[:_]");
            if (fields.length != 2) {
                results.add(ValidationResult.of(inputValidator(), ValidationLevel.ERROR,
                        "Term ID syntax", // TODO ????????????????????
                        String.format("Malformed ontology term: \"%s\"", id)));
            }
            String termPrefix = fields[0];
            if (! validOntologyPrefixes.contains(termPrefix)) {
                results.add(ValidationResult.of(inputValidator(), ValidationLevel.ERROR,
                        "Ontology Not In MetaData", // TODO ????????????????????
                        String.format("No ontology corresponding to term: \"%s\" found in MetaData", id)));
            }

        }
        return results;
    }



}
