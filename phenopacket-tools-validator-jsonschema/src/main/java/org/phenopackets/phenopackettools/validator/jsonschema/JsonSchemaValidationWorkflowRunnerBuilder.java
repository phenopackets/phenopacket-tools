package org.phenopackets.phenopackettools.validator.jsonschema;

import com.google.protobuf.MessageOrBuilder;
import org.phenopackets.phenopackettools.validator.core.PhenopacketValidator;
import org.phenopackets.phenopackettools.validator.core.ValidationWorkflowRunnerBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder for {@link JsonSchemaValidationWorkflowRunner}.
 * <p>
 * Build the {@link JsonSchemaValidationWorkflowRunner} for running base validation, and additional JSON schema-based
 * validation (provide either {@link Path}s or {@link URL}s to JSON schema documents),
 * and {@link PhenopacketValidator}s for performing additional validation.
 *
 * @param <T> one of top-level elements of the Phenopacket schema.
 */
public abstract class JsonSchemaValidationWorkflowRunnerBuilder<T extends MessageOrBuilder> extends ValidationWorkflowRunnerBuilder<T> {

    protected final List<URL> jsonSchemaUrls = new ArrayList<>();

    protected JsonSchemaValidationWorkflowRunnerBuilder() {
        // private no-op
    }

    /**
     * Register a JSON schema present at a given {@code path} to be used as a validator. The {@code path}
     * will be interpreted as a {@link URL}.
     *
     * @param path path to the JSON schema document
     * @return the builder
     * @throws MalformedURLException if the {@code path} cannot be converted to a well-formatted {@link URL}
     */
    public JsonSchemaValidationWorkflowRunnerBuilder<T> addJsonSchema(Path path) throws MalformedURLException {
        return addJsonSchema(path.toUri().toURL());
    }

    /**
     * Register a JSON schema present at a given {@code url} to be used as a validator.
     *
     * @param url url to the JSON schema document
     * @return the builder
     */
    public JsonSchemaValidationWorkflowRunnerBuilder<T> addJsonSchema(URL url) {
        jsonSchemaUrls.add(url);
        return this;
    }

    /**
     * Add JSON schemas in bulk.
     *
     * @param paths an iterable of paths pointing to JSON schema documents
     * @return the builder
     * @see JsonSchemaValidationWorkflowRunnerBuilder#addJsonSchema(Path)
     */
    public JsonSchemaValidationWorkflowRunnerBuilder<T> addAllJsonSchemaPaths(Iterable<Path> paths) throws MalformedURLException {
        for (Path path : paths) {
            jsonSchemaUrls.add(path.toUri().toURL());
        }
        return this;
    }

    /**
     * Add JSON schemas in bulk.
     *
     * @param urls an iterable of urls pointing to JSON schema documents
     * @return the builder
     * @see JsonSchemaValidationWorkflowRunnerBuilder#addJsonSchema(URL)
     */
    public JsonSchemaValidationWorkflowRunnerBuilder<T> addAllJsonSchemaUrls(List<URL> urls) {
        jsonSchemaUrls.addAll(urls);
        return this;
    }

    /**
     * Finish building the {@link JsonSchemaValidationWorkflowRunner}.
     *
     * @return the runner
     */
    @Override
    public abstract JsonSchemaValidationWorkflowRunner<T> build();

}
