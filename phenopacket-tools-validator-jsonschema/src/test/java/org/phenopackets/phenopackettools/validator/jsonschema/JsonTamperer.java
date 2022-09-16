package org.phenopackets.phenopackettools.validator.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonTamperer {

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
                    } else {
                        int i = Integer.parseInt(idx);
                        quantifier = NodeQuantifier.indices(Set.of(i));
                        if (current.isArray())
                            current = current.get(i);
                        else
                            throw new RuntimeException("Unable to get %d-th element of node '%s' with %d elements".formatted(i, name, current.size()));
                    }

                    // TODO - allow performing action on one or more child attributes
                    break;
                }
            }
        }

        if (name == null) {
            throw new RuntimeException("Blaaah");
        }

        switch (action) {
            case DELETE -> {
                if (quantifier == null) {
                    ObjectNode objectParent = (ObjectNode) parent;
                    objectParent.remove(name);
                } else {
                    ArrayNode array = (ArrayNode) current;
                    for (int i = array.size() - 1; i >= 0; i--) {
                        if (quantifier.shouldDeleteNode(i))
                            array.remove(i);
                    }
                }
            }
        }

        return node;
    }

    private record NodeQuantifier(boolean allNodes, Set<Integer> indices) {

        static NodeQuantifier allValues() {
            return new NodeQuantifier(true, Set.of());
        }

        static NodeQuantifier indices(Collection<Integer> indices) {
            return new NodeQuantifier(false, Set.copyOf(indices));
        }

        boolean shouldDeleteNode(int idx) {
            return allNodes || indices.contains(idx);
        }
    }

}
