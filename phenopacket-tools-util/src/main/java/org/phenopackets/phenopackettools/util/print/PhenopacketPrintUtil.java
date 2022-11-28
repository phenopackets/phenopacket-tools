package org.phenopackets.phenopackettools.util.print;

import com.google.protobuf.Descriptors;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.schema.v2.Cohort;
import org.phenopackets.schema.v2.Family;
import org.phenopackets.schema.v2.Phenopacket;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A static utility class for obtaining {@linkplain JsonFormat.Parser} and {@linkplain JsonFormat.Printer}
 * configured for printing Phenopacket Schema elements.
 */
public class PhenopacketPrintUtil {

    // There are no special requirements for the parser as of now.
    // However, we keep it here for consistency.
    private static final JsonFormat.Parser PARSER = JsonFormat.parser();

    private static final JsonFormat.Printer PRINTER = JsonFormat.printer()
            .includingDefaultValueFields(defaultValueFields());

    private static Set<Descriptors.FieldDescriptor> defaultValueFields() {
        /*
        We must ensure that we print the enum values even if the value is the default value.
        Otherwise, the base validation will fail due to absence of a required field.

        The set that we create below includes all enum descriptors declared in protobuf files.
         */
        return Stream.of(
                        findEnumDescriptors(Phenopacket.getDescriptor()),
                        findEnumDescriptors(Family.getDescriptor()),
                        findEnumDescriptors(Cohort.getDescriptor())
                )
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    /**
     * Get a parser configured for parsing Phenopacket Schema elements from JSON format.
     */
    public static JsonFormat.Parser getParser() {
        return PARSER;
    }

    /**
     * Get a printer configured for printing Phenopacket Schema elements into JSON while respecting
     * the special requirements of the schema.
     * <p>
     *     Currently, the special requirements include printing all enum field values, including the default values
     *     whose presence is implied in absence of the JSON field by Protobuf.
     * </p>
     *
     * @return the printer
     */
    public static JsonFormat.Printer getPrinter() {
        return PRINTER;
    }

    /**
     * Find recursively all enum field descriptors, starting from {@code base}.
     */
    private static Stream<Descriptors.FieldDescriptor> findEnumDescriptors(Descriptors.Descriptor base) {
        Stream.Builder<Descriptors.FieldDescriptor> builder = Stream.builder();
        Set<Descriptors.Descriptor> visited = new HashSet<>();
        allEnumDescriptors(base, builder, visited);

        return builder.build();
    }

    private static void allEnumDescriptors(Descriptors.Descriptor descriptor,
                                           Stream.Builder<Descriptors.FieldDescriptor> builder,
                                           Set<Descriptors.Descriptor> visited) {
        for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
            if (field.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.ENUM))
                builder.add(field);

            if (field.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.MESSAGE)) {
                if (visited.contains(field.getMessageType()))
                    continue;

                visited.add(field.getMessageType());
                allEnumDescriptors(field.getMessageType(), builder, visited);
            }
        }
    }

    private PhenopacketPrintUtil() {
        // static no-op
    }

}
