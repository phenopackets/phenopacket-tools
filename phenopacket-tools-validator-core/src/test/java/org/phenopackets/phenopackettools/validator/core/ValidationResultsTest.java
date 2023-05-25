package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ValidationResultsTest {

    @Test
    public void serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<ValidatorInfo> validatorInfos = List.of(
                ValidatorInfo.of("ID1", "NAME1", "DESCRIPTION1"),
                ValidatorInfo.of("ID2", "NAME2", "DESCRIPTION2")
        );
        List<ValidationResult> data = List.of(
                ValidationResult.of(validatorInfos.get(0), ValidationLevel.ERROR, "CATEGORY1", "MESSAGE1"),
                ValidationResult.of(validatorInfos.get(1), ValidationLevel.WARNING, "CATEGORY2", "MESSAGE2")
        );
        ValidationResults results = ValidationResults.of(validatorInfos, data);

        assertThat(mapper.writeValueAsString(results), equalTo("""
                {
                  "validators" : [ {
                    "validatorId" : "ID1",
                    "validatorName" : "NAME1",
                    "description" : "DESCRIPTION1"
                  }, {
                    "validatorId" : "ID2",
                    "validatorName" : "NAME2",
                    "description" : "DESCRIPTION2"
                  } ],
                  "validationResults" : [ {
                    "validatorInfo" : {
                      "validatorId" : "ID1",
                      "validatorName" : "NAME1",
                      "description" : "DESCRIPTION1"
                    },
                    "level" : "ERROR",
                    "category" : "CATEGORY1",
                    "message" : "MESSAGE1"
                  }, {
                    "validatorInfo" : {
                      "validatorId" : "ID2",
                      "validatorName" : "NAME2",
                      "description" : "DESCRIPTION2"
                    },
                    "level" : "WARNING",
                    "category" : "CATEGORY2",
                    "message" : "MESSAGE2"
                  } ]
                }"""));
    }
}