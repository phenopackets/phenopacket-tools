package org.phenopackets.phenopackettools.validator.core.phenotype;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.Objects;
import java.util.stream.Stream;

abstract class BaseHpoPhenotypeValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "HpoPhenotypeValidator",
            "HPO phenotypic feature validator",
            "Validate that HPO terms are well formatted, present, and non-obsolete based on the provided HPO");
    private static final String INVALID_TERM_ID = "Invalid TermId";
    private static final String OBSOLETED_TERM_ID = "Obsoleted TermId";

    private final Ontology hpo;
    private final String hpoVersion;

    public BaseHpoPhenotypeValidator(Ontology hpo) {
        this.hpo = Objects.requireNonNull(hpo);
        this.hpoVersion = this.hpo.getMetaInfo().getOrDefault("data-version", "HPO");
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    protected Stream<? extends ValidationResult> checkPhenotypeFeature(String individualId, PhenotypicFeature feature) {
        TermId termId;
        try {
            termId = TermId.of(feature.getType().getId());
        } catch (PhenolRuntimeException e) {
            // Should not really happen if JsonSchema validators are run upstream, but let's stay safe.
            String msg = "The %s found in '%s' is not a valid value".formatted(feature.getType().getId(), individualId);
            return Stream.of(
                    ValidationResult.error(VALIDATOR_INFO, INVALID_TERM_ID, msg)
            );
        }

        // Check if the HPO contains the term.
        if (!hpo.containsTerm(termId)) {
            String msg = "%s in '%s' not found in %s".formatted(termId.getValue(), individualId, hpoVersion);
            return Stream.of(
                    ValidationResult.error(VALIDATOR_INFO, INVALID_TERM_ID, msg)
            );
        }

        // Check if the `termId` is a primary ID. // If not, this is a warning.
        TermId primaryId = hpo.getPrimaryTermId(termId);
        if (!primaryId.equals(termId)) {
            String msg = "Using obsoleted id (%s) instead of current primary id (%s) in '%s'"
                    .formatted(termId.getValue(), primaryId.getValue(), individualId);
            return Stream.of(
                    ValidationResult.warning(VALIDATOR_INFO, OBSOLETED_TERM_ID, msg)
            );
        }

        return Stream.empty();
    }

}
