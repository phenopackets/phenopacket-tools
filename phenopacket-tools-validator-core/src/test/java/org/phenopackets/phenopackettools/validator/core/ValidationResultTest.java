package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ValidationResultTest {

    @Test
    public void serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        ValidatorInfo info = ValidatorInfo.of("ID", "NAME", "DESCRIPTION");
        ValidationResult result = ValidationResult.of(info, ValidationLevel.ERROR, "CATEGORY", "MESSAGE");

        assertThat(mapper.writeValueAsString(result),
                equalTo("""
                        {
                          "validatorInfo" : {
                            "validatorId" : "ID",
                            "validatorName" : "NAME",
                            "description" : "DESCRIPTION"
                          },
                          "level" : "ERROR",
                          "category" : "CATEGORY",
                          "message" : "MESSAGE"
                        }""")
        );
    }
}