package org.phenopackets.phenotools.validator.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Phenopacket validator applies rules to check that the provided data meets the requirements for a valid Phenopacket.
 * <p>
 * @author Daniel Danis
 * @author Peter N Robinson
 */
public interface PhenopacketValidator {

    ValidatorInfo info();

    List<ValidationItem> validate(InputStream inputStream);

    // -----------------------------------------------------------------------------------------------------------------

    default List<ValidationItem> validate(File phenopacket) throws IOException {
        try (InputStream inputStream = Files.newInputStream(phenopacket.toPath())) {
            return validate(inputStream);
        }
    }

    default List<ValidationItem> validate(String content) {
        return validate(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }

}
