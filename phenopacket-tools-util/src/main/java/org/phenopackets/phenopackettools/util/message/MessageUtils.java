package org.phenopackets.phenopackettools.util.message;

import com.google.protobuf.MessageOrBuilder;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A class with utility functions for working with protobuf messages.
 */
public class MessageUtils {

    private MessageUtils() {
        // static utility class
    }

    /**
     * Find all instances of {@link T} in a given {@code message}.
     * <p>
     * Protobuf messages are hierarchical structures where, depending on the schema, a component may be present
     * at different places of the hierarchy. This function will stream all instances of the requested class.
     *
     * @param message protobuf message to search in
     * @param clz target class
     * @return a {@linkplain Stream} of all {@link T}s found in the {@code message}
     * @param <T> type of the target class
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> findInstancesOfType(MessageOrBuilder message, Class<T> clz) {
        Stream.Builder<T> builder = Stream.builder();
        if (clz.isInstance(message)) {
            /*
             We suppress the warning regarding an unchecked cast of the message to T because we have checked
             that `clz.isInstance(message)` holds.
             */
            builder.add((T) message);
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
}
