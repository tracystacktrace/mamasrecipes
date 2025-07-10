package net.tracystacktrace.mamasrecipes.constructor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeProcessException extends Exception {
    //item parse exceptions
    public static final byte ITEM_IDENTIFIER_NOT_FOUND = -128;
    public static final byte INVALID_ITEM_ID = -127;
    public static final byte INVALID_ITEM_COUNT = -126;
    public static final byte INVALID_ITEM_META = -125;
    public static final byte INVALID_ITEM_DISPLAY_NAME = -124;

    //recipe parse exceptions
    public static final byte INCORRECT_RECIPE_TYPE = -123;
    public static final byte INVALID_RECIPE_PATTERN = -122;
    public static final byte RECIPE_PATTERN_NOT_FOUND = -121;
    public static final byte INVALID_RECIPE_KEYS = -120;
    public static final byte INVALID_RECIPE_KEY = -119;
    public static final byte RECIPE_KEYS_NOT_FOUND = -118;
    public static final byte INVALID_RECIPE_RESULT = -117;
    public static final byte RECIPE_RESULT_NOT_FOUND = -116;
    public static final byte INVALID_RECIPE_TYPE = -115;

    public static @NotNull String getMessage(byte code, @Nullable String optional) {
        switch (code) {
            //item builder
            case ITEM_IDENTIFIER_NOT_FOUND:
                return "An item identifier/ID was expected, but not found!";
            case INVALID_ITEM_ID:
                return String.format("Invalid item ID/Identifier: %s", optional);
            case INVALID_ITEM_COUNT:
                return String.format("Invalid item count: %s", optional);
            case INVALID_ITEM_META:
                return String.format("Invalid item meta: %s", optional);
            case INVALID_ITEM_DISPLAY_NAME:
                return String.format("Invalid item display name: %s", optional);
            //recipes builder
            case INCORRECT_RECIPE_TYPE:
                return String.format("Incorrect recipe type! Received: %s", optional);
            case INVALID_RECIPE_PATTERN:
                return String.format("Invalid recipe pattern: %s", optional);
            case RECIPE_PATTERN_NOT_FOUND:
                return "A recipe pattern was expected, but not found!";
            case INVALID_RECIPE_KEYS:
                return String.format("Invalid recipe keys: %s", optional);
            case INVALID_RECIPE_KEY:
                return String.format("Received an invalid recipe key: %s", optional);
            case RECIPE_KEYS_NOT_FOUND:
                return "A recipe keys set was expected, but not found!";
            case INVALID_RECIPE_RESULT:
                return String.format("Invalid recipe result: %s", optional);
            case RECIPE_RESULT_NOT_FOUND:
                return "A recipe result was expected, but not found!";
            case INVALID_RECIPE_TYPE:
                return String.format("Invalid recipe type: %s", optional);
            //default
            default:
                return "Unknown error!";
        }
    }

    public RecipeProcessException(byte code, @Nullable String optional, @Nullable Exception e) {
        super(getMessage(code, optional), e);
    }

    public RecipeProcessException(byte code, @Nullable String optional) {
        this(code, optional, null);
    }

    public RecipeProcessException(byte code) {
        this(code, null, null);
    }

    public RecipeProcessException(byte code, @Nullable Exception e) {
        this(code, null, e);
    }
}
