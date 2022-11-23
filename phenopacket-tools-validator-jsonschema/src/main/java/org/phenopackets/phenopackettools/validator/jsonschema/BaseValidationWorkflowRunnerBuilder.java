package org.phenopackets.phenopackettools.validator.jsonschema;

import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverter;
import org.phenopackets.phenopackettools.validator.core.PhenopacketFormatConverters;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.metadata.MetaDataValidators;
import org.phenopackets.phenopackettools.validator.jsonschema.impl.JsonSchemaValidator;
import org.phenopackets.phenopackettools.validator.jsonschema.v2.JsonSchemaValidatorConfigurer;
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
 * A utility class that provides {@link JsonSchemaValidationWorkflowRunnerBuilder} implementations for top-level
 * elements of Phenopacket schema.
 * <p>
 * The class exists because we do not want to expose {@link JsonSchemaValidator} to the outside world.
 */
abstract class BaseValidationWorkflowRunnerBuilder<T extends MessageOrBuilder> extends JsonSchemaValidationWorkflowRunnerBuilder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseValidationWorkflowRunnerBuilder.class);

    @Override
    public JsonSchemaValidationWorkflowRunner<T> build() {
        List<JsonSchemaValidator> requirementValidators = readRequirementValidators(jsonSchemaUrls);
        return new JsonSchemaValidationWorkflowRunner<>(getFormatConverter(),
                getBaseRequirementsValidator(),
                getMetadataValidator(),
                requirementValidators,
                validators);
    }


    protected abstract PhenopacketFormatConverter<T> getFormatConverter();
    protected abstract JsonSchemaValidator getBaseRequirementsValidator();
    protected abstract PhenopacketValidator<T> getMetadataValidator();


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

    static class PhenopacketWorkflowRunnerBuilder extends BaseValidationWorkflowRunnerBuilder<PhenopacketOrBuilder> {

        @Override
        protected PhenopacketFormatConverter<PhenopacketOrBuilder> getFormatConverter() {
            return PhenopacketFormatConverters.phenopacketConverter();
        }

        @Override
        protected JsonSchemaValidator getBaseRequirementsValidator() {
            return JsonSchemaValidatorConfigurer.getBasePhenopacketValidator();
        }

        @Override
        protected PhenopacketValidator<PhenopacketOrBuilder> getMetadataValidator() {
            return MetaDataValidators.phenopacketValidator();
        }
    }

    static class FamilyWorkflowRunnerBuilder extends BaseValidationWorkflowRunnerBuilder<FamilyOrBuilder> {
        @Override
        protected PhenopacketFormatConverter<FamilyOrBuilder> getFormatConverter() {
            return PhenopacketFormatConverters.familyConverter();
        }

        @Override
        protected JsonSchemaValidator getBaseRequirementsValidator() {
            return JsonSchemaValidatorConfigurer.getBaseFamilyValidator();
        }

        @Override
        protected PhenopacketValidator<FamilyOrBuilder> getMetadataValidator() {
            return MetaDataValidators.familyValidator();
        }
    }


    static class CohortWorkflowRunnerBuilder extends BaseValidationWorkflowRunnerBuilder<CohortOrBuilder> {
        @Override
        protected PhenopacketFormatConverter<CohortOrBuilder> getFormatConverter() {
            return PhenopacketFormatConverters.cohortConverter();
        }

        @Override
        protected JsonSchemaValidator getBaseRequirementsValidator() {
            return JsonSchemaValidatorConfigurer.getBaseCohortValidator();
        }

        @Override
        protected PhenopacketValidator<CohortOrBuilder> getMetadataValidator() {
            return MetaDataValidators.cohortValidator();
        }
    }
}
