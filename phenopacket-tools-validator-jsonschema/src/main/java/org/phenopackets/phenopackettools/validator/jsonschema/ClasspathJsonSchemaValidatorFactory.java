package org.phenopackets.phenopackettools.validator.jsonschema;

import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidatorFactory;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorRuntimeException;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Validator factory that uses JSON schema definitions that are bundled within the application (on classpath).
 * <p>
 *
 * @author Daniel Danis
 * @author Peter N Robinson
 */
public class ClasspathJsonSchemaValidatorFactory implements PhenopacketValidatorFactory {

    private final Map<ValidatorInfo, JsonSchemaValidator> validatorMap;

    public static ClasspathJsonSchemaValidatorFactory defaultValidators() {
        Map<ValidatorInfo, JsonSchemaValidator> validatorMap = makeValidatorMap();
        return new ClasspathJsonSchemaValidatorFactory(validatorMap);
    }

    private static Map<ValidatorInfo, JsonSchemaValidator> makeValidatorMap() {
        return Map.of(
                ValidatorInfo.generic(), makeJsonValidator("/schema/phenopacket-schema-2-0.json", ValidatorInfo.generic()),
                ValidatorInfo.rareDiseaseValidation(), makeJsonValidator("/schema/hpo-rare-disease-schema.json", ValidatorInfo.rareDiseaseValidation())
        );
    }

    private static JsonSchemaValidator makeJsonValidator(String schemaPath, ValidatorInfo validationName) {
        InputStream inputStream = ClasspathJsonSchemaValidatorFactory.class.getResourceAsStream(schemaPath);
        if (inputStream == null)
            throw new PhenopacketValidatorRuntimeException("Invalid JSON schema path `" + schemaPath + '`');

        return JsonSchemaValidator.of(inputStream, validationName);
    }

    private ClasspathJsonSchemaValidatorFactory(Map<ValidatorInfo, JsonSchemaValidator> validatorMap) {
        this.validatorMap = validatorMap;
    }

    @Override
    public Optional<PhenopacketValidator> getValidatorForType(ValidatorInfo type) {
        return Optional.ofNullable(validatorMap.get(type));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClasspathJsonSchemaValidatorFactory that = (ClasspathJsonSchemaValidatorFactory) o;
        return Objects.equals(validatorMap, that.validatorMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validatorMap);
    }

}
