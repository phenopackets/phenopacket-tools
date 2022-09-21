package org.phenopackets.phenopackettools.validator.core.metadata;

import com.google.protobuf.Descriptors;
import org.phenopackets.phenopackettools.validator.core.ValidationLevel;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Resource;

import java.util.*;

import static org.phenopackets.phenopackettools.validator.core.ValidatorInfo.inputValidator;

public class PhenopacketMetaDataValidator extends BaseMetaDataValidator<PhenopacketOrBuilder>  {



    @Override
    public List<ValidationResult> validate(PhenopacketOrBuilder phenopacket) {
        List<ValidationResult> results = new ArrayList<>();
        MetaData metaData = phenopacket.getMetaData();
        List<OntologyClass> instances = new ArrayList<>();
        Map<Descriptors.FieldDescriptor, Object> allFields = phenopacket.getAllFields();
        for (var e : allFields.entrySet()) {
            Descriptors.FieldDescriptor fdesc = e.getKey();
            if (fdesc.getName().equals("OntologyClass")) {
                instances.add((OntologyClass) e.getValue());
            }
        }
        Set<String> validOntologyPrefixes = new HashSet<>();
        for (Resource resource : metaData.getResourcesList()) {
            validOntologyPrefixes.add(resource.getNamespacePrefix());
        }
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
