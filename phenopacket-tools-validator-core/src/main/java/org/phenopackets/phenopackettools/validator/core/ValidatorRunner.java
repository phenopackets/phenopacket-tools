package org.phenopackets.phenopackettools.validator.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ValidatorRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorRunner.class);

   // private final PhenopacketValidatorFactory validatorFactory;
    private final List<PhenopacketMessageValidator> messageValidators;
    private final List<JsonPhenopacketValidator> jsonValidators;


   // private final ValidationWorkflowRunner phenopacketValidationWorkflowRunner;
    // private final ValidationWorkflowRunner cohortValidationWorkflowRunner;
    // private final ValidationWorkflowRunner familyValidationWorkflowRunner;

//    private final List<CohortValidator> cohortValidators;
//
//    private final List<FamilyValidator> familyValidators;

    public ValidatorRunner(List<PhenopacketMessageValidator> validators,
                           List<JsonPhenopacketValidator> jsonPhenopacketValidators) {
        this.messageValidators = validators;
        this.jsonValidators = jsonPhenopacketValidators;
       // cohortValidators = List.of();

    }






    public List<ValidationItem> validatePhenopacket(InputStream stream) {
        // 1. Create json string
        // phenopacketValidationWorkflowRunner.run()

        return List.of();
    }

    public List<ValidationItem> validateCohort(InputStream stream) {
        /// 1. Create json string
        // 2. cohortValidationWorkflowRunner.run()


        return List.of();
    }



        public List<ValidationItem> validate(InputStream inputStream, List<PhenopacketMessageValidator> validations) {
        List<ValidationItem> items = new ArrayList<>();
        //JsonNode json = objectMapper.readTree(inputStream);
        // loop 1
        //PhenopacketBuilder from google
        // loop 2
        try {
            byte[] content = inputStream.readAllBytes();
           // items =  validations.stream().flatMap(v -> v.validate(inputStream).stream())
           //         .toList();
//            for (PhenopacketValidator validationType : validations) {
//               // validatorFactory.getValidatorForType(validationType)
//                        validate(content).
//                        //.ifPresent(items::addAll);
//            }
            return List.copyOf(items);

        } catch (IOException e) {
            LOGGER.error("Error occurred during validation {}", e.getMessage(), e);
        }
        return List.of();
    }

    private static Function<PhenopacketValidatorOld, List<ValidationItem>> validate(byte[] content) {
        return validator -> {
            try (InputStream is = new ByteArrayInputStream(content)) {
                return validator.validate(is);
            } catch (IOException e) {
                LOGGER.error("Error occurred during validation {}", e.getMessage(), e);
            }
            return List.of();
        };
    }


}
