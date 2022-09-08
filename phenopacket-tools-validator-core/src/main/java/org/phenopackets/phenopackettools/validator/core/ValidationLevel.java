package org.phenopackets.phenopackettools.validator.core;

public enum ValidationLevel {

    /**
     * The level for pointing out an issue pertaining to a <em>recommended</em> field as described in the
     * <a href="https://phenopacket-schema.readthedocs.io/en/master/requirements.html">Phenopacket Schema requirements</a>
     * <p>
     * Briefly, the field is not absolutely required or there are valid reasons in particular circumstances that
     * the field does not apply to the intended use case of the Phenopacket.
     * This corresponds to the keywords <em>SHOULD</em> and <em>RECOMMENDED</em>
     * in <a href="https://www.ietf.org/rfc/rfc2119.txt">RFC2119</a>.
     */
    WARNING,

    /**
     * The level for pointing out an issue pertaining to a <em>required</em> field as described in the
     * <a href="https://phenopacket-schema.readthedocs.io/en/master/requirements.html">Phenopacket Schema requirements</a>
     * <p>
     * Briefly, the field is required, its presence is an absolute requirement of the specification, failing which
     * the entire phenopacket is regarded as malformed.
     * This corresponds to the keywords <em>MUST</em>, <em>REQUIRED</em>, and <em>SHALL</em>
     * in <a href="https://www.ietf.org/rfc/rfc2119.txt">RFC2119</a>.
     */
    ERROR

}
