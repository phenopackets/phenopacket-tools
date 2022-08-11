package org.phenopackets.phenopackettools.validator.core;



import java.util.List;

public class DefaultValidationRunner implements ValidationWorkflowRunner {

    public DefaultValidationRunner(List<JsonPhenopacketValidator> sds, List<PhenopacketMessageValidator> v1) {

    }

    /**
     *  implementation:
     * 1. get string as input
     * 2. decide if JSON/YML/Protobuf ("sniff the input")
     * 3. Parse into JSON string
     * 4. Fail if the JSON is not Phenopacket (e.g., fail for Family or Cohort) (one interface, three impl)
     * 5. Pass to list of JSON Schema Validators
     * 6. Convert JSON to Phenopacket, Cohort, or Family
     * 7. Run all PhenopacketMessageValidators
     * 8. Return List<ValidationItem>
     * @return
     */
    @Override
    public List<ValidationItem> run(String input) {
        //
        return null;
    }
}
