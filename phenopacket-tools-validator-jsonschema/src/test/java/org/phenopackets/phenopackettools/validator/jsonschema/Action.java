package org.phenopackets.phenopackettools.validator.jsonschema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {

    // An action that has an instruction (e.g. SET, DELETE) followed by an optional content to represent a value to be set
    // tot the appropriate JSON node
    private static final Pattern PATTERN = Pattern.compile("^(?<instruction>\\w+)(\\[(?<content>-?\\w+)])?$");

    private static final Action DELETE = new Action(ActionType.DELETE, null);

    private final ActionType actionType;
    private final Object content;

    public static Action valueOf(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (matcher.matches()) {
            String instruction = matcher.group("instruction");
            if ("set".equalsIgnoreCase(instruction)) {
                Object content = parseContent(matcher.group("content"));
                return set(content);
            } else if ("delete".equalsIgnoreCase(instruction)) {
                return DELETE;
            } else {
                throw new IllegalArgumentException("Unrecognized instruction '%s'".formatted(value));
            }
        }
        throw new IllegalArgumentException("Unable to parse value '%s'".formatted(value));
    }

    private static Object parseContent(String content) {
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            return content;
        }
    }

    public static Action delete() {
        return DELETE;
    }

    public static Action set(Object content) {
        return new Action(ActionType.SET, content);
    }

    private Action(ActionType actionType, Object content) {
        this.actionType = actionType;
        this.content = content;
    }
    public boolean isDelete() {
        return ActionType.DELETE.equals(actionType);
    }

    public boolean isSet() {
        return ActionType.SET.equals(actionType);
    }

    public Object content() {
        return content;
    }

    private enum ActionType {
        DELETE,
        SET
    }

    @Override
    public String toString() {
        return "Action{" +
                "actionType=" + actionType +
                ", content=" + content +
                '}';
    }
}
