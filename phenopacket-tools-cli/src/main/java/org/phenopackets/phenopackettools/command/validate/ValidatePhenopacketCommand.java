package org.phenopackets.phenopackettools.command.validate;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import picocli.CommandLine;

import java.net.URL;
import java.util.List;

/**
 * Command to run {@link PhenopacketOrBuilder} validation.
 */
@CommandLine.Command(name = "phenopacket",
        description = "Validate phenopackets using builtin and custom JSON Schemas.",
        mixinStandardHelpOptions = true)
public class ValidatePhenopacketCommand extends BaseValidateCommand<PhenopacketOrBuilder> {

    @Override
    protected ValidationWorkflowRunner<PhenopacketOrBuilder> prepareValidationWorkflow(List<URL> customJsonSchemas,
                                                                                       List<PhenopacketValidator<PhenopacketOrBuilder>> semanticValidators) {
        return JsonSchemaValidationWorkflowRunner.phenopacketBuilder()
                .addAllJsonSchemaUrls(customJsonSchemas)
                .addAllSemanticValidators(semanticValidators)
                .build();
    }

    @Override
    protected PhenopacketValidator<PhenopacketOrBuilder> createHpoValidator(Ontology hpo) {
        return HpoPhenotypeValidators.phenopacketHpoPhenotypeValidator(hpo);
    }

}
