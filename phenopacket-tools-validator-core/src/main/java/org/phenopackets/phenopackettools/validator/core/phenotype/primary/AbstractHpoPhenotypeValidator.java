package org.phenopackets.phenopackettools.validator.core.phenotype.primary;

import com.google.protobuf.MessageOrBuilder;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.phenopackets.phenopackettools.validator.core.*;
import org.phenopackets.phenopackettools.validator.core.phenotype.base.BaseHpoValidator;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractHpoPhenotypeValidator<T extends MessageOrBuilder> extends BaseHpoValidator<T> {

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "HpoPhenotypeValidator",
            "HPO phenotypic feature validator",
            "Validate that HPO terms are well formatted, present, and non-obsolete based on the provided HPO");
    private static final String INVALID_TERM_ID = "Invalid TermId";
    private static final String OBSOLETED_TERM_ID = "Obsoleted TermId";

    public AbstractHpoPhenotypeValidator(MinimalOntology hpo) {
        super(hpo);
    }

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    protected Stream<? extends ValidationResult> checkPhenotypeFeature(PhenopacketOrBuilder phenopacket, PhenotypicFeature feature) {
        TermId termId;
        try {
            termId = TermId.of(feature.getType().getId());
        } catch (PhenolRuntimeException e) {
            String idSummary = summarizePhenopacketAndIndividualId(phenopacket);
            // Should not really happen if JsonSchema validators are run upstream, but let's stay safe.
            String msg = "The %s found%s is not a valid term ID".formatted(feature.getType().getId(), idSummary);
            return Stream.of(
                    ValidationResult.error(VALIDATOR_INFO, INVALID_TERM_ID, msg)
            );
        }
        if (termId.getPrefix().equals("HP")) {
            // Check if the HPO contains the term.
            Optional<Term> term = hpo.termForTermId(termId);
            if (term.isEmpty()) {
                String idSummary = summarizePhenopacketAndIndividualId(phenopacket);
                String msg = "%s%s not found in %s".formatted(termId.getValue(), idSummary, hpoVersion);
                return Stream.of(
                        ValidationResult.error(VALIDATOR_INFO, INVALID_TERM_ID, msg)
                );
            }

            // Check if the `termId` is a primary ID. // If not, this is a warning.
            TermId primaryId = term.get().id();
            if (!primaryId.equals(termId)) {
                String idSummary = summarizePhenopacketAndIndividualId(phenopacket);
                String msg = "Using obsolete id (%s) instead of current primary id (%s)%s".formatted(
                        termId.getValue(), primaryId.getValue(), idSummary);
                return Stream.of(
                        ValidationResult.warning(VALIDATOR_INFO, OBSOLETED_TERM_ID, msg)
                );
            }
        }

        return Stream.empty();
    }

}
