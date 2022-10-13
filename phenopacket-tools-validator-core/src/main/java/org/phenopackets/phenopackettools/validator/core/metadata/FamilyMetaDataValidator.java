package org.phenopackets.phenopackettools.validator.core.metadata;

import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.core.MetaData;

class FamilyMetaDataValidator extends BaseMetaDataValidator<FamilyOrBuilder> {

    @Override
    protected MetaData getMetadata(FamilyOrBuilder message) {
        return message.getMetaData();
    }

}
