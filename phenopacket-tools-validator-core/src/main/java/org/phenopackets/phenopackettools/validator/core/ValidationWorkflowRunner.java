package org.phenopackets.phenopackettools.validator.core;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface ValidationWorkflowRunner {


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

    List<ValidationResult> validate(InputStream io);
   List<ValidationResult> validate(byte[] content);
    List<ValidationResult> validate(byte[] content, Charset charset);

    List<ValidationResult> validate(String content);

    List<ValidationResult> validate(File file);



}
