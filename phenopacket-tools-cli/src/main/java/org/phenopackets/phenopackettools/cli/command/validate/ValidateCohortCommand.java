package org.phenopackets.phenopackettools.cli.command.validate;

import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunner;
import org.phenopackets.phenopackettools.validator.core.phenotype.HpoPhenotypeValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.JsonSchemaValidationWorkflowRunner;
import org.phenopackets.schema.v2.CohortOrBuilder;
import picocli.CommandLine;

import java.net.URL;
import java.util.List;

/**
 * Command to run {@link CohortOrBuilder} validation.
 */
@CommandLine.Command(name = "cohort",
        description = "Validate cohorts using builtin and custom JSON Schemas.",
        mixinStandardHelpOptions = true)
@Deprecated(forRemoval = true)
public class ValidateCohortCommand extends BaseValidateCommand<CohortOrBuilder> {

    @Override
    protected ValidationWorkflowRunner<CohortOrBuilder> prepareValidationWorkflow(List<URL> customJsonSchemas,
                                                                                  List<PhenopacketValidator<CohortOrBuilder>> semanticValidators) {
        return JsonSchemaValidationWorkflowRunner.cohortBuilder()
                .addAllJsonSchemaUrls(customJsonSchemas)
                .addAllSemanticValidators(semanticValidators)
                .build();
    }

    @Override
    protected PhenopacketValidator<CohortOrBuilder> createHpoValidator(Ontology hpo) {
        return HpoPhenotypeValidators.cohortHpoPhenotypeValidator(hpo);
    }

}
