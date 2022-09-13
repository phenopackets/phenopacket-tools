package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Validates data format, the basic requirements defined by a JSON schema, and semantic requirements
 * defined by a {@link Collection} of {@link PhenopacketValidator}s.
 *
 * @param <T> must be one of the three top-level elements of the Phenopacket schema.
 */
public class JsonSchemaValidationWorkflowRunner<T extends MessageOrBuilder> implements ValidationWorkflowRunner<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PhenopacketFormatConverter<T> converter;
    private final JsonSchemaValidator requirementsValidator;
    private final Collection<PhenopacketValidator<T>> semanticValidators;

    public JsonSchemaValidationWorkflowRunner(PhenopacketFormatConverter<T> converter,
                                              JsonSchemaValidator requirementsValidator,
                                              Collection<PhenopacketValidator<T>> semanticValidators) {
        // TODO - the user should not be forced to provide the converter value, it should be an implementation detail.
        this.converter = Objects.requireNonNull(converter);
        this.requirementsValidator = Objects.requireNonNull(requirementsValidator);
        this.semanticValidators = Objects.requireNonNull(semanticValidators);
    }

    @Override
    public ValidationResults validate(byte[] payload) {
        ValidationResults.Builder builder = ValidationResults.builder();

        String json;
        try {
            json = parseJson(payload);
        } catch (ConversionException e) {
            // we cannot proceed without a valid JSON string.
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

    private String parseJson(byte[] payload) throws ConversionException {
        if (Util.looksLikeJson(payload)) {
            return new String(payload);
        } else {
            // Must be protobuf bytes, otherwise we explode.
            return converter.toJson(payload);
        }
    }

    /**
     * Validate requirements using {@link #requirementsValidator}.
     *
     * @throws ConversionException if {@code json} cannot be mapped into {@link JsonNode}.
     */
    private void validateRequirements(String json, ValidationResults.Builder builder) throws ConversionException {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new ConversionException(e);
        }

        builder.addResults(requirementsValidator.validatorInfo(), requirementsValidator.validate(jsonNode));
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

}
