package org.phenopackets.phenopackettools.validator.core;

import com.google.protobuf.MessageOrBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The base builder for constructing {@link ValidationWorkflowRunner}.
 * The builder keeps track of the validators and builds the {@link ValidationWorkflowRunner} at the end.
 *
 * @param <T> type of the top-level element of the Phenopacket Schema.
 */
public abstract class ValidationWorkflowRunnerBuilder<T extends MessageOrBuilder> {

    protected final List<PhenopacketValidator<T>> validators = new ArrayList<>();
    /**
     * @deprecated use {@link #validators} instead
     */
    @Deprecated(forRemoval = true, since = "0.4.8")
    protected final List<PhenopacketValidator<T>> syntaxValidators = new ArrayList<>();
    /**
     * @deprecated use {@link #validators} instead
     */
    @Deprecated(forRemoval = true, since = "0.4.8")
    protected final List<PhenopacketValidator<T>> semanticValidators = new ArrayList<>();

    /**
     * Add a validator to the end of the workflow.
     */
    public ValidationWorkflowRunnerBuilder<T> addValidator(PhenopacketValidator<T> validator) {
        this.validators.add(validator);
        return this;
    }

    /**
     * Add validators to the end of the workflow.
     */
    public ValidationWorkflowRunnerBuilder<T> addValidators(Collection<? extends PhenopacketValidator<T>> validators) {
        this.validators.addAll(validators);
        return this;
    }

    /**
     * Add a syntax validator.
     *
     * @param syntaxValidator the syntax validator
     * @return the builder
     * @deprecated use {@link #addValidator(PhenopacketValidator)} instead
     */
    @Deprecated(forRemoval = true, since = "0.4.8")
    public ValidationWorkflowRunnerBuilder<T> addSyntaxValidator(PhenopacketValidator<T> syntaxValidator) {
        this.validators.add(syntaxValidator);
        return this;
    }

    /**
     * Add syntax validators in bulk.
     *
     * @param validators the syntax validators
     * @return the builder
     * @deprecated use {@link #addValidators(Collection)} instead
     */
    @Deprecated(forRemoval = true, since = "0.4.8")
    public ValidationWorkflowRunnerBuilder<T> addAllSyntaxValidators(List<PhenopacketValidator<T>> validators) {
        // A slightly more efficient implementation comparing to the default method on the interface.
        this.validators.addAll(validators);
        return this;
    }

    /**
     * Add a semantic validator.
     *
     * @param semanticValidator the semantic validator
     * @return the builder
     * @deprecated use {@link #addValidator(PhenopacketValidator)} instead
     */
    @Deprecated(forRemoval = true, since = "0.4.8")
    public ValidationWorkflowRunnerBuilder<T> addSemanticValidator(PhenopacketValidator<T> semanticValidator) {
        this.validators.add(semanticValidator);
        return this;
    }

    /**
     * Add semantic validators in bulk.
     *
     * @param validators the semantic validators
     * @return the builder
     * @deprecated use {@link #addValidators(Collection)} instead
     */
    @Deprecated(forRemoval = true, since = "0.4.8")
    public ValidationWorkflowRunnerBuilder<T> addAllSemanticValidators(List<PhenopacketValidator<T>> validators) {
        // A slightly more efficient implementation comparing to the default method on the interface.
        this.validators.addAll(validators);
        return this;
    }

    /**
     * Finish building of the {@link ValidationWorkflowRunner}.
     *
     * @return the runner
     */
    public abstract ValidationWorkflowRunner<T> build();

}
