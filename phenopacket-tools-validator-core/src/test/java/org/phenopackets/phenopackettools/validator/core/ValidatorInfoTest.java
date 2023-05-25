package org.phenopackets.phenopackettools.validator.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ValidatorInfoTest {

    @Test
    public void serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        ValidatorInfo info = ValidatorInfo.of("ID", "NAME", "DESCRIPTION");

        assertThat(mapper.writeValueAsString(info), equalTo("""
                {
                  "validatorId" : "ID",
                  "validatorName" : "NAME",
                  "description" : "DESCRIPTION"
                }""")
        );
    }
}