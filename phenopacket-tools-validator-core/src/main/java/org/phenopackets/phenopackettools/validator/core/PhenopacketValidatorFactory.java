package org.phenopackets.phenopackettools.validator.core;

import java.util.Optional;

/**
 * The phenopacket validator factory provides phenopacket validators designed to perform specific {@link ValidatorInfo}.
 * <p>
 * @author Daniel Danis
 * @author Peter N Robinson
 */
@Deprecated(forRemoval = true)
public interface PhenopacketValidatorFactory {
    // TODO probably simplify/delete
    Optional<PhenopacketValidatorOld> getValidatorForType(ValidatorInfo type);
    // ? List<PhenopacketValidator> getValidators();

}
