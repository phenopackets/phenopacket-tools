package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Test;
import org.phenopackets.phenopackettools.validator.core.except.PhenopacketValidatorInputException;
import org.phenopackets.schema.v2.Phenopacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultPhenopacketIngestorTest {

    private static final String minimalPhenopacket = """
            {
              "id": "arbitrary.id",
              "subject": {
                "id": "proband A",
                "timeAtLastEncounter": {
                  "age": {
                    "iso8601duration": "P39Y"
                  }
                },
                "sex": "FEMALE"
              },
              "diseases": [{
                "term": {
                  "id": "MONDO:0007915",
                  "label": "systemic lupus erythematosus"
                },
                "onset": {
                  "age": {
                    "iso8601duration": "P39Y"
                  }
                }
              }],
              "metaData": {
                "created": "2021-05-14T10:35:00Z",
                "createdBy": "anonymous biocurator",
                "resources": [{
                  "id": "uberon",
                  "name": "Uber-anatomy ontology",
                  "url": "http://purl.obolibrary.org/obo/uberon.owl",
                  "version": "2021-07-27",
                  "namespacePrefix": "UBERON",
                  "iriPrefix": "http://purl.obolibrary.org/obo/UBERON_"
                }, {
                  "id": "ncbitaxon",
                  "name": "NCBI organismal classification",
                  "url": "http://purl.obolibrary.org/obo/ncbitaxon.owl",
                  "version": "2021-06-10",
                  "namespacePrefix": "NCBITaxon",
                  "iriPrefix": "http://purl.obolibrary.org/obo/NCBITaxon_"
                }],
                "phenopacketSchemaVersion": "2.0"
              }
            }""";


    @Test
    public void testCtor() throws PhenopacketValidatorInputException, InvalidProtocolBufferException {
        var builder = Phenopacket.newBuilder();
        JsonFormat.parser().merge(minimalPhenopacket, builder);
        var pp = builder.build();
        assertEquals("arbitrary.id", pp.getId());
    }

}
