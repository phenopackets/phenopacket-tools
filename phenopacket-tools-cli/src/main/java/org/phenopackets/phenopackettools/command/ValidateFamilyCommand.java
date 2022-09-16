package org.phenopackets.phenopackettools.command;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import picocli.CommandLine;

import java.net.URL;
import java.util.List;

/**
 * Command to run {@link FamilyOrBuilder} validation.
 */
@CommandLine.Command(name = "family",
        description = "Validate families using builtin and custom JSON Schemas.",
        mixinStandardHelpOptions = true)
public class ValidateFamilyCommand extends BaseValidateCommand<FamilyOrBuilder> {

    @Override
    protected ValidationWorkflowRunner<FamilyOrBuilder> prepareValidationWorkflow(List<URL> customJsonSchemas,
                                                                                  List<PhenopacketValidator<FamilyOrBuilder>> semanticValidators) {
         return JsonSchemaValidationWorkflowRunner.familyBuilder()
                .addAllJsonSchemaUrls(customJsonSchemas)
                .addAllSemanticValidators(semanticValidators)
                .build();
    }

    @Override
    protected PhenopacketValidator<FamilyOrBuilder> createHpoValidator(Ontology hpo) {
        return HpoPhenotypeValidators.familyHpoPhenotypeValidator(hpo);
    }

}
