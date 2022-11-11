package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The base builder for constructing {@link ValidationWorkflowRunner}. The builder keeps track of
 * the <em>syntax</em> and <em>semantic</em> validators.
 *
 * @param <T> type of the top-level element of the Phenopacket Schema.
 */
public abstract class ValidationWorkflowRunnerBuilder<T extends MessageOrBuilder> {

    protected final List<PhenopacketValidator<T>> syntaxValidators = new ArrayList<>();
    protected final List<PhenopacketValidator<T>> semanticValidators = new ArrayList<>();

    /**
     * Add a syntax validator.
     *
     * @param syntaxValidator the syntax validator
     * @return the builder
     */
    public ValidationWorkflowRunnerBuilder<T> addSyntaxValidator(PhenopacketValidator<T> syntaxValidator) {
        this.syntaxValidators.add(syntaxValidator);
        return this;
    }

    /**
     * Add syntax validators in bulk.
     *
     * @param validators the syntax validators
     * @return the builder
     */
    public ValidationWorkflowRunnerBuilder<T> addAllSyntaxValidators(List<PhenopacketValidator<T>> validators) {
        // A slightly more efficient implementation comparing to the default method on the interface.
        this.syntaxValidators.addAll(validators);
        return this;
    }

    /**
     * Add a semantic validator.
     *
     * @param semanticValidator the semantic validator
     * @return the builder
     */
    public ValidationWorkflowRunnerBuilder<T> addSemanticValidator(PhenopacketValidator<T> semanticValidator) {
        this.semanticValidators.add(semanticValidator);
        return this;
    }

    /**
     * Add semantic validators in bulk.
     *
     * @param validators the semantic validators
     * @return the builder
     */
    public ValidationWorkflowRunnerBuilder<T> addAllSemanticValidators(List<PhenopacketValidator<T>> validators) {
        // A slightly more efficient implementation comparing to the default method on the interface.
        this.semanticValidators.addAll(validators);
        return this;
    }

    /**
     * Finish building of the {@link ValidationWorkflowRunner}.
     *
     * @return the runner
     */
    public abstract ValidationWorkflowRunner<T> build();

}
