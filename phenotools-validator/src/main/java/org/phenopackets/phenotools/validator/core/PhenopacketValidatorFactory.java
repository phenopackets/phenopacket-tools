package org.phenopackets.phenotools.validator.core;

import java.util.Optional;

/**
 * The phenopacket validator factory provides phenopacket validators designed to perform specific {@link ValidatorInfo}.
 * <p>
 * @author Daniel Danis
 * @author Peter N Robinson
 */
public interface PhenopacketValidatorFactory {

    Optional<? extends PhenopacketValidator> getValidatorForType(ValidatorInfo type);

}
