package org.phenopackets.phenopackettools.validator.core.metadata;

import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationResult;
import org.phenopackets.phenopackettools.validator.core.ValidatorInfo;
import org.phenopackets.schema.v2.CohortOrBuilder;
import org.phenopackets.schema.v2.FamilyOrBuilder;
import org.phenopackets.schema.v2.PhenopacketOrBuilder;
import org.phenopackets.schema.v2.core.MetaData;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class MetaDataValidator<T extends MessageOrBuilder> implements PhenopacketValidator<T> {

    private static final ValidatorInfo VALIDATOR_INFO = ValidatorInfo.of(
            "MetaDataValidator",
            "MetaData validator",
            "Validate that the MetaData section describes all used ontologies");

    @Override
    public ValidatorInfo validatorInfo() {
        return VALIDATOR_INFO;
    }

    @Override
    public List<ValidationResult> validate(T component) {
        // Validate that these fields use ontology prefixes that are represented in the MetaData section.
        Optional<MetaData> metaData = getMetadata(component);
        if (metaData.isEmpty())
            // No need to run MetaData validation if there is no metadata!
            return List.of();

        Set<String> validOntologyPrefixes = getOntologyNamespacePrefixes(metaData.get());

        return streamOfAllInstancesOfType(component, OntologyClass.class).sequential()
                .flatMap(oc -> {
                    // Curie should be something like `HP:1234567` or `NCIT_C123457`.
                    String curie = oc.getId();
                    String[] fields = curie.split("[:_]");

                    if (fields.length != 2) {
                        return Stream.of(ValidationResult.error(VALIDATOR_INFO,
                                "Ontology class ID syntax",
                                "Malformed ontology class ID '%s'".formatted(curie)
                        ));
                    }

                    String prefix = fields[0];
                    if (!validOntologyPrefixes.contains(prefix)) {
                        return Stream.of(ValidationResult.error(VALIDATOR_INFO,
                                "Ontology Not In MetaData",
                                "No ontology corresponding to ID '%s' found in MetaData".formatted(curie)
                        ));
                    }
                    return Stream.empty();
                })
                .toList();
    }

    /**
     * A hook for getting {@link MetaData} from Phenopacket schema top-level element.
     * The {@code Optional} is empty if {@link MetaData} is not initialized or
     * is equal to {@link MetaData#getDefaultInstance()} (hence not useful for validation).
     */
    protected abstract Optional<MetaData> getMetadata(T message);

    private static Set<String> getOntologyNamespacePrefixes(MetaData metaData) {
        return metaData.getResourcesList()
                .stream()
                .map(Resource::getNamespacePrefix)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private static <U> Stream<U> streamOfAllInstancesOfType(MessageOrBuilder message, Class<U> clz) {
        Stream.Builder<U> builder = Stream.builder();
        if (clz.isInstance(message)) {
            /*
             We suppress the warning regarding an unchecked cast of the message to T because we have checked
             that `clz.isInstance(message)` holds.
             */
            builder.add((U) message);
        }

        for (Object field : message.getAllFields().values()) {
            findAllInstances(field, clz, builder);
        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private static <U> void findAllInstances(Object o, Class<U> clz, Stream.Builder<U> builder) {
        if (clz.isInstance(o)) {
            /*
             We suppress the warning regarding an unchecked cast of `o` to T because we have checked
             that `clz.isInstance(o)` holds.
             */
            builder.add((U) o);
        } else {
            if (o instanceof MessageOrBuilder message) {
                for (Object v : message.getAllFields().values()) {
                    findAllInstances(v, clz, builder);
                }
            } else if (o instanceof Collection<?> collection) {
                for (Object nestedField : collection) {
                    findAllInstances(nestedField, clz, builder);
                }
            }
        }
    }

    static class PhenopacketMetaDataValidator extends MetaDataValidator<PhenopacketOrBuilder> {

        @Override
        protected Optional<MetaData> getMetadata(PhenopacketOrBuilder message) {
            return !message.getMetaData().isInitialized() || message.getMetaData().equals(MetaData.getDefaultInstance())
                    ? Optional.empty()
                    : Optional.of(message.getMetaData());
        }

    }

    static class FamilyMetaDataValidator extends MetaDataValidator<FamilyOrBuilder> {

        @Override
        protected Optional<MetaData> getMetadata(FamilyOrBuilder message) {
            return !message.getMetaData().isInitialized() || message.getMetaData().equals(MetaData.getDefaultInstance())
                    ? Optional.empty()
                    : Optional.of(message.getMetaData());
        }

    }

    static class CohortMetaDataValidator extends MetaDataValidator<CohortOrBuilder> {

        @Override
        protected Optional<MetaData> getMetadata(CohortOrBuilder message) {
            return !message.getMetaData().isInitialized() || message.getMetaData().equals(MetaData.getDefaultInstance())
                    ? Optional.empty()
                    : Optional.of(message.getMetaData());
        }

    }
}
