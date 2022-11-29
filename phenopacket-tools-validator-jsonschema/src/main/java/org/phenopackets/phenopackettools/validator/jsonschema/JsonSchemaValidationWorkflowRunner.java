package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.util.format.FormatSniffer;
import org.phenopackets.phenopackettools.core.PhenopacketFormat;
import org.phenopackets.phenopackettools.util.format.FormatSniffException;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.jsonschema.impl.JsonSchemaValidator;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Validates if given top-level element satisfies the following criteria:
 * <ul>
 *     <li><b>data format requirements</b> - for instance if the element is a valid JSON document if JSON input is provided</li>
 *     <li><b>basic Phenopacket schema syntax requirements</b> - the requirements described by the reference documentation.
 *     Absence of a <em>required</em> field is an {@link ValidationLevel#ERROR} and absence of a recommended field is
 *     a {@link ValidationLevel#WARNING},</li>
 *     <li><b>custom requirements</b> - requirements provided in a JSON schema document(s) provided by the user or
 *     provided as <em>ad hoc</em> {@link PhenopacketValidator}s.</li>
 * </ul>
 * <p>
 * The validation is performed in the order as outlined above. Note that the data format validation must
 * pass in order for the latter steps to run.
 * <p>
 * Use one of {@link JsonSchemaValidationWorkflowRunnerBuilder}s provided via static constructors (e.g. {@link #phenopacketBuilder()}) to build
 * the validation workflow.
 *
 * @param <T> must be one of the three top-level elements of the Phenopacket schema:
 *            {@link org.phenopackets.schema.v2.PhenopacketOrBuilder}, {@link org.phenopackets.schema.v2.FamilyOrBuilder},
 *            or {@link org.phenopackets.schema.v2.CohortOrBuilder}.
 */
public class JsonSchemaValidationWorkflowRunner<T extends MessageOrBuilder> implements ValidationWorkflowRunner<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PhenopacketFormatConverter<T> converter;
    private final JsonSchemaValidator baseValidator;
    private final PhenopacketValidator<T> metadataValidator;
    private final Collection<JsonSchemaValidator> requirementValidators;
    private final Collection<PhenopacketValidator<T>> validators;
    private final List<ValidatorInfo> validatorInfos;

    /**
     * @return a {@link JsonSchemaValidationWorkflowRunnerBuilder} for building a {@link JsonSchemaValidationWorkflowRunner} for validating
     * {@link PhenopacketOrBuilder}.
     */
    public static JsonSchemaValidationWorkflowRunnerBuilder<PhenopacketOrBuilder> phenopacketBuilder() {
        return new BaseValidationWorkflowRunnerBuilder.PhenopacketWorkflowRunnerBuilder();
    }

    /**
     * @return a {@link JsonSchemaValidationWorkflowRunnerBuilder} for building a {@link JsonSchemaValidationWorkflowRunner} for validating
     * {@link FamilyOrBuilder}.
     */
    public static JsonSchemaValidationWorkflowRunnerBuilder<FamilyOrBuilder> familyBuilder() {
        return new BaseValidationWorkflowRunnerBuilder.FamilyWorkflowRunnerBuilder();
    }

    /**
     * @return a {@link JsonSchemaValidationWorkflowRunnerBuilder} for building a {@link JsonSchemaValidationWorkflowRunner} for validating
     * {@link CohortOrBuilder}
     */
    public static JsonSchemaValidationWorkflowRunnerBuilder<CohortOrBuilder> cohortBuilder() {
        return new BaseValidationWorkflowRunnerBuilder.CohortWorkflowRunnerBuilder();
    }

    JsonSchemaValidationWorkflowRunner(PhenopacketFormatConverter<T> converter,
                                       JsonSchemaValidator baseValidator,
                                       PhenopacketValidator<T> metadataValidator,
                                       Collection<JsonSchemaValidator> requirementValidators,
                                       Collection<PhenopacketValidator<T>> validators) {
        this.converter = Objects.requireNonNull(converter);
        this.baseValidator = Objects.requireNonNull(baseValidator);
        this.metadataValidator = Objects.requireNonNull(metadataValidator);
        this.requirementValidators = Objects.requireNonNull(requirementValidators);
        this.validators = Objects.requireNonNull(validators);
        this.validatorInfos = summarizeValidatorInfos(baseValidator, metadataValidator, requirementValidators, validators);
    }

    private static <T extends MessageOrBuilder> List<ValidatorInfo> summarizeValidatorInfos(JsonSchemaValidator base,
                                                                                            PhenopacketValidator<T> metadataValidator,
                                                                                            Collection<JsonSchemaValidator> requirements,
                                                                                            Collection<PhenopacketValidator<T>> validators) {
        List<ValidatorInfo> infos = new ArrayList<>();

        infos.add(base.validatorInfo());
        infos.add(metadataValidator.validatorInfo());
        for (JsonSchemaValidator validator : requirements) {
            infos.add(validator.validatorInfo());
        }

        for (PhenopacketValidator<T> validator : validators) {
            infos.add(validator.validatorInfo());
        }

        return List.copyOf(infos);
    }

    @Override
    public List<ValidatorInfo> validators() {
        return validatorInfos;
    }

    @Override
    public ValidationResults validate(byte[] payload) {
        ValidationResults.Builder builder = ValidationResults.builder();

        String json;
        try {
            json = parseToString(payload);
        } catch (ConversionException e) {
            // data format validation failed - we cannot proceed without a valid JSON string.
            return wrapUpValidation(e, builder);
        }

        return validate(json);
    }

    @Override
    public ValidationResults validate(String json) {
        ValidationResults.Builder builder = ValidationResults.builder();

        T component;
        try {
            component = converter.toItem(json);
            validateRequirements(json, component, builder);
        } catch (ConversionException e) {
            // We must not proceed with semantic validation with item that does not meet the requirements.
            return wrapUpValidation(e, builder);
        }

        return runValidation(component, builder);
    }

    @Override
    public ValidationResults validate(T item) {
        ValidationResults.Builder builder = ValidationResults.builder();

        String json = converter.toJson(item);

        try {
            validateRequirements(json, item, builder);
        } catch (ConversionException e) {
            // We must not proceed with semantic validation with item that does not meet the requirements.
            return wrapUpValidation(e, builder);
        }

        // No conversion necessary, hence no need to guard against the `ConversionException`.
        return runValidation(item, builder);
    }

    private String parseToString(byte[] payload) throws ConversionException {
        try {
            PhenopacketFormat format = FormatSniffer.sniff(payload);
            return switch (format) {
                case JSON, YAML -> new String(payload);
                case PROTOBUF -> converter.toJson(payload);
            };
        } catch (FormatSniffException e) {
            throw new ConversionException(e);
        }
    }

    /**
     * Validate the requirements using {@link #baseValidator} and all {@link #requirementValidators}.
     *
     * @throws ConversionException if {@code json} cannot be mapped into {@link JsonNode}
     */
    private void validateRequirements(String json, T component, ValidationResults.Builder builder) throws ConversionException {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            // data format validation failed - the `String` cannot be parsed into a `JsonNode`.
            throw new ConversionException(e);
        }

        builder.addResults(baseValidator.validatorInfo(), baseValidator.validate(jsonNode));
        builder.addResults(metadataValidator.validatorInfo(), metadataValidator.validate(component));

        for (JsonSchemaValidator validator : requirementValidators) {
            builder.addResults(validator.validatorInfo(), validator.validate(jsonNode));
        }
    }

    /**
     * Validate the requirements by applying {@link #validators}.
     */
    private ValidationResults runValidation(T component, ValidationResults.Builder builder) {
        for (PhenopacketValidator<T> validator : validators)
            builder.addResults(validator.validatorInfo(), validator.validate(component));

        return builder.build();
    }

    private static ValidationResults wrapUpValidation(ConversionException e, ValidationResults.Builder builder) {
        return builder.addResult(e.validatorInfo(), e)
                .build();
    }

}
