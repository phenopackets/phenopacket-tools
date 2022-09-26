package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonTamperer {

    // A pattern to match a single path component.
    // For path `/path/to[4]/node` will yield 3 matches: "/path", "/to[4]", and "/node".
    private static final Pattern STEP = Pattern.compile("/(?<name>\\w+)(\\[(?<idx>(\\d+)|(\\*))])?");

    public JsonNode tamper(JsonNode node, String path, Action action) {
        Matcher matcher = STEP.matcher(path);
        JsonNode parent = null;
        JsonNode current = node;
        String name = null, idx;
        NodeQuantifier quantifier = null;
        while (matcher.find()) {
            parent = current;
            name = matcher.group("name");
            idx = matcher.group("idx");
            if (current.has(name)) {
                current = current.get(name);
                if (idx != null) {
                    // If we get here we're tampering with an array node, as opposed to an object node.
                    if ("*".equals(idx)) {
                        quantifier = NodeQuantifier.allValues();
                        // We can only process one
                        break;
                    } else {
                        int i = Integer.parseInt(idx);
                        quantifier = NodeQuantifier.index(i);
                        if (current.isArray()) {
                            parent = current;
                            current = current.get(i);
                            quantifier = null;
                        } else {
                            throw new RuntimeException("Unable to get %d-th element of node '%s' with %d elements".formatted(i, name, current.size()));
                        }
                    }
                }
            }
        }

        if (name == null) {
            // TODO - address properly
            throw new RuntimeException("Blaaah");
        }

        if (action.isDelete()) {
            if (quantifier == null) {
                // We're deleting a simple node attribute. This only works on object nodes.
                if (parent instanceof ObjectNode objectNode)
                    objectNode.remove(name);
                else
                    throw new RuntimeException("The penultimate node on path %s should have been an object node but it was not".formatted(path));
            } else {
                // We're deleting some/all elements of an array. This only works on array nodes.
                if (current instanceof ArrayNode array) {
                    for (int i = array.size() - 1; i >= 0; i--) {
                        // We loop in reverse to do not mess up the indices.
                        if (quantifier.shouldDeleteNode(i))
                            array.remove(i);
                    }
                } else
                    throw new RuntimeException("The last node on path %s should have been an array node but it was not".formatted(path));
            }
        } else if (action.isSet()) {
            Object content = action.content();

            // We're setting a simple node attribute. This only works on object nodes.
            if (parent instanceof ObjectNode objectNode) {
                if (content instanceof Integer i) {
                    objectNode.set(name, IntNode.valueOf(i));
                } else if (content instanceof Double d) {
                    objectNode.set(name, DoubleNode.valueOf(d));
                } else if (content instanceof String s) {
                    objectNode.set(name, TextNode.valueOf(s));
                } else
                    throw new RuntimeException("Unsupported content type %s".formatted(content.getClass().getName()));
            } else {
                throw new RuntimeException("The penultimate node on path %s should have been an object node but it was not".formatted(path));
            }
        } else {
            throw new RuntimeException("Unsupported action.");
        }

        return node;
    }

    private record NodeQuantifier(boolean allNodes, int index) {

        static NodeQuantifier allValues() {
            return new NodeQuantifier(true, -1);
        }

        static NodeQuantifier index(int index) {
            return new NodeQuantifier(false, index);
        }

        boolean shouldDeleteNode(int idx) {
            return allNodes || idx == index;
        }
    }

}
