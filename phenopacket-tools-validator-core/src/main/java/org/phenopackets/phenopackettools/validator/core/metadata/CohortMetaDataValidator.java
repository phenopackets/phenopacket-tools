package org.phenopackets.phenopackettools.validator.core.metadata;

import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.core.MetaData;

class CohortMetaDataValidator extends BaseMetaDataValidator<CohortOrBuilder> {

    @Override
    protected MetaData getMetadata(CohortOrBuilder message) {
        return message.getMetaData();
    }

}
