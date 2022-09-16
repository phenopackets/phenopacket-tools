package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Validates if given top-level element satisfies the following criteria:
 * <ul>
 *     <li><b>data format requirements</b> - for instance if the element is a valid JSON document if JSON input is provided</li>
 *     <li><b>basic Phenopacket schema requirements</b> - the requirements described by the reference documentation.
 *     Absence of a <em>required</em> field is an {@link ValidationLevel#ERROR} and absence of a recommended field is
 *     a {@link ValidationLevel#WARNING}.</li>
 *     <li><b>custom requirements</b> - requirements provided in a JSON schema document(s) provided by the user.</li>
 *     <li><b>semantic requirements</b> - requirements checked by {@link PhenopacketValidator}s provided by the user.</li>
 * </ul>
 * <p>
 * The validation is performed in steps as outlined by the list above. Note that the data format validation must
 * pass in order for the latter steps to run.
 * <p>
 * Use one of {@link Builder}s provided via static constructors (e.g. {@link #phenopacketBuilder()}) to build
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
    private final Collection<JsonSchemaValidator> requirementValidators;
    private final Collection<PhenopacketValidator<T>> semanticValidators;

    /**
     * @return a {@link Builder} for building a {@link JsonSchemaValidationWorkflowRunner} for validating
     * {@link PhenopacketOrBuilder}.
     */
    public static Builder<PhenopacketOrBuilder> phenopacketBuilder() {
        return new ValidationWorkflowRunnerBuilder.PhenopacketWorkflowRunnerBuilder();
    }

    /**
     * @return a {@link Builder} for building a {@link JsonSchemaValidationWorkflowRunner} for validating
     * {@link FamilyOrBuilder}.
     */
    public static Builder<FamilyOrBuilder> familyBuilder() {
        return new ValidationWorkflowRunnerBuilder.FamilyWorkflowRunnerBuilder();
    }

    /**
     * @return a {@link Builder} for building a {@link JsonSchemaValidationWorkflowRunner} for validating
     * {@link CohortOrBuilder}.
     */
    public static Builder<CohortOrBuilder> cohortBuilder() {
        return new ValidationWorkflowRunnerBuilder.CohortWorkflowRunnerBuilder();
    }

    JsonSchemaValidationWorkflowRunner(PhenopacketFormatConverter<T> converter,
                                       JsonSchemaValidator baseValidator,
                                       Collection<JsonSchemaValidator> requirementValidators,
                                       Collection<PhenopacketValidator<T>> semanticValidators) {
        this.converter = Objects.requireNonNull(converter);
        this.baseValidator = Objects.requireNonNull(baseValidator);
        this.requirementValidators = Objects.requireNonNull(requirementValidators);
        this.semanticValidators = Objects.requireNonNull(semanticValidators);
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

        try {
            validateRequirements(json, builder);
        } catch (ConversionException e) {
            return wrapUpValidation(e, builder);
        }

        try {
            validateSemantic(json, builder);
        } catch (ConversionException e) {
            return wrapUpValidation(e, builder);
        }

        return builder.build();
    }

    @Override
    public ValidationResults validate(T item) {
        ValidationResults.Builder builder = ValidationResults.builder();

        String json = converter.toJson(item);

        try {
            validateRequirements(json, builder);
        } catch (ConversionException e) {
            // We must not proceed with semantic validation with item that does not meet the requirements.
            return wrapUpValidation(e, builder);
        }

        // No conversion necessary, hence no need to guard against the `ConversionException`.
        validateSemantic(item, builder);

        return builder.build();
    }

    private String parseToString(byte[] payload) throws ConversionException {
        if (Util.looksLikeJson(payload)) {
            return new String(payload);
        } else {
            // Must be protobuf bytes, otherwise we explode.
            return converter.toJson(payload);
        }
    }

    /**
     * Validate requirements using {@link #baseValidator} and all {@link #requirementValidators}.
     *
     * @throws ConversionException if {@code json} cannot be mapped into {@link JsonNode}.
     */
    private void validateRequirements(String json, ValidationResults.Builder builder) throws ConversionException {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            // data format validation failed - the `String` cannot be parsed into a `JsonNode`.
            throw new ConversionException(e);
        }

        builder.addResults(baseValidator.validatorInfo(), baseValidator.validate(jsonNode));

        for (JsonSchemaValidator validator : requirementValidators) {
            builder.addResults(validator.validatorInfo(), validator.validate(jsonNode));
        }
    }

    /**
     * Validate semantic requirements using {@link #semanticValidators}.
     *
     * @throws ConversionException if {@code item} cannot be mapped into {@link T}.
     */
    private void validateSemantic(String item, ValidationResults.Builder builder) throws ConversionException {
        T component = converter.toItem(item);

        validateSemantic(component, builder);
    }

    /**
     * Validate semantic requirements using {@link #semanticValidators}. Unlike {@link #validateSemantic(String, ValidationResults.Builder)},
     * this method does not throw {@link ConversionException}.
     */
    private void validateSemantic(T component, ValidationResults.Builder builder) {
        for (PhenopacketValidator<T> validator : semanticValidators)
            builder.addResults(validator.validatorInfo(), validator.validate(component));
    }

    private static ValidationResults wrapUpValidation(ConversionException e, ValidationResults.Builder builder) {
        return builder.addResult(e.validatorInfo(), e)
                .build();
    }

    /**
     * A builder for {@link JsonSchemaValidationWorkflowRunner}.
     * <p>
     * Build the {@link JsonSchemaValidationWorkflowRunner} by providing JSON schema documents
     * either as {@link Path} or {@link URL}s, and {@link PhenopacketValidator}s for performing semantic validation.
     *
     * @param <T> one of top-level elements of the Phenopacket schema.
     */
    public static abstract class Builder<T extends MessageOrBuilder> {

        protected final List<URL> jsonSchemaUrls = new ArrayList<>();
        protected final List<PhenopacketValidator<T>> semanticValidators = new ArrayList<>();

        protected Builder() {
            // private no-op
        }

        public Builder<T> addJsonSchema(Path path) throws MalformedURLException {
            return addJsonSchema(path.toUri().toURL());
        }

        public Builder<T> addJsonSchema(URL url) {
            jsonSchemaUrls.add(url);
            return this;
        }

        public Builder<T> addAllJsonSchemaPaths(List<Path> paths) throws MalformedURLException {
            for (Path path : paths) {
                jsonSchemaUrls.add(path.toUri().toURL());
            }
            return this;
        }

        public Builder<T> addAllJsonSchemaUrls(List<URL> urls) {
            jsonSchemaUrls.addAll(urls);
            return this;
        }

        public Builder<T> addSemanticValidator(PhenopacketValidator<T> semanticValidator) {
            this.semanticValidators.add(semanticValidator);
            return this;
        }

        public Builder<T> addAllSemanticValidators(List<PhenopacketValidator<T>> semanticValidators) {
            this.semanticValidators.addAll(semanticValidators);
            return this;
        }

        public abstract JsonSchemaValidationWorkflowRunner<T> build();

    }

}
