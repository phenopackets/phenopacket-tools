package org.phenopackets.phenopackettools.validator.jsonschema;

import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverter;
import org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverters;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that provides {@link JsonSchemaValidationWorkflowRunner.Builder} implementations for top-level
 * elements of Phenopacket schema.
 * <p>
 * The class exists because we do not want to expose {@link JsonSchemaValidator} to the outside world.
 */
abstract class ValidationWorkflowRunnerBuilder<T extends MessageOrBuilder> extends JsonSchemaValidationWorkflowRunner.Builder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationWorkflowRunnerBuilder.class);

    @Override
    public JsonSchemaValidationWorkflowRunner<T> build() {
        List<JsonSchemaValidator> requirementValidators = readRequirementValidators(jsonSchemaUrls);
        return new JsonSchemaValidationWorkflowRunner<>(getFormatConverter(),
                getBaseRequirementsValidator(),
                requirementValidators,
                semanticValidators);
    }


    protected abstract PhenopacketFormatConverter<T> getFormatConverter();
    protected abstract JsonSchemaValidator getBaseRequirementsValidator();


    private List<JsonSchemaValidator> readRequirementValidators(List<URL> schemaUrls) {
        List<JsonSchemaValidator> requirementValidators = new ArrayList<>();
        for (URL schemaUrl : schemaUrls) {
            LOGGER.debug("Opening JSON schema at '{}'", schemaUrl);
            try (InputStream is = schemaUrl.openStream()) {
                JsonSchemaValidator validator = JsonSchemaValidatorConfigurer.configureJsonSchemaValidator(is);
                requirementValidators.add(validator);
            } catch (IOException e) {
                LOGGER.warn("Error when configuring requirement validator based on schema at {}: {}. See debug for more info", schemaUrl, e.getMessage());
                LOGGER.debug("Error when configuring requirement validator based on schema at {}: {}", schemaUrl, e.getMessage(), e);
            }
        }
        return requirementValidators;
    }

    static class PhenopacketWorkflowRunnerBuilder extends ValidationWorkflowRunnerBuilder<PhenopacketOrBuilder> {

        @Override
        protected PhenopacketFormatConverter<PhenopacketOrBuilder> getFormatConverter() {
            return PhenopacketFormatConverters.phenopacketConverter();
        }

        @Override
        protected JsonSchemaValidator getBaseRequirementsValidator() {
            return JsonSchemaValidatorConfigurer.getBasePhenopacketValidator();
        }
    }

    static class FamilyWorkflowRunnerBuilder extends ValidationWorkflowRunnerBuilder<FamilyOrBuilder> {
        @Override
        protected PhenopacketFormatConverter<FamilyOrBuilder> getFormatConverter() {
            return PhenopacketFormatConverters.familyConverter();
        }

        @Override
        protected JsonSchemaValidator getBaseRequirementsValidator() {
            return JsonSchemaValidatorConfigurer.getBaseFamilyValidator();
        }
    }


    static class CohortWorkflowRunnerBuilder extends ValidationWorkflowRunnerBuilder<CohortOrBuilder> {
        @Override
        protected PhenopacketFormatConverter<CohortOrBuilder> getFormatConverter() {
            return PhenopacketFormatConverters.cohortConverter();
        }

        @Override
        protected JsonSchemaValidator getBaseRequirementsValidator() {
            return JsonSchemaValidatorConfigurer.getBaseCohortValidator();
        }
    }
}
