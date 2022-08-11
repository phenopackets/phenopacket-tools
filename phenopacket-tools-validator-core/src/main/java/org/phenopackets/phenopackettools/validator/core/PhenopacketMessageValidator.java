package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.Message;



import java.util.List;

/**
 * Interface for validator classes that do not use JSON Schema, for instance,
 * classes that use Ontology logic.
 * @param <T> A Message that can be a Phenopacket, Family, Cohort, or components of these messages
 */
public interface PhenopacketMessageValidator<T extends Message> {


        ValidatorInfo info();

        List<ValidationItem> validate(T ga4ghPhenopacketMessage);

}


