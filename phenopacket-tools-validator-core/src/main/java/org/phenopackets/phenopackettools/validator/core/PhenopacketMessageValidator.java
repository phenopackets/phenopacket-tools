package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.Message;



import java.util.List;

public interface PhenopacketMessageValidator<T extends Message> {


        ValidatorInfo info();

        List<ValidationItem> validate(T ga4ghPhenopacketMessage);




}


