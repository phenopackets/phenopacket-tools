package org.phenopackets.phenopackettools.validator.core.metadata;

import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.MetaData;

class PhenopacketMetaDataValidator extends BaseMetaDataValidator<PhenopacketOrBuilder>  {

    @Override
    protected MetaData getMetadata(PhenopacketOrBuilder message) {
        return message.getMetaData();
    }

}
