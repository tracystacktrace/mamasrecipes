package net.tracystacktrace.mamasrecipes.walk;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WalkException extends Exception {
    public static final byte NOT_A_FOLDER = -128;
    public static final byte FOLDER_READ_FAILED = -127;
    public static final byte FILE_READ_FAILED = -126;
    public static final byte INVALID_JSON_FILE = -125;
    public static final byte RECIPE_PROCESS_FAILED = -124;

    public static @NotNull String getErrorMessage(byte code, @Nullable String optional) {
        switch (code) {
            case NOT_A_FOLDER: return String.format("Not a folder: %s", optional);
            case FOLDER_READ_FAILED: return String.format("Failed to walk through a folder: %s", optional);
            case FILE_READ_FAILED: return String.format("Failed to read a file: %s", optional);
            case INVALID_JSON_FILE: return String.format("Invalid JSON file: %s", optional);
            case RECIPE_PROCESS_FAILED: return String.format("Failed to process recipe file: %s", optional);

            default: return "Unknown error!";
        }
    }

    public WalkException(byte code, @Nullable String optional, @Nullable Exception e) {
        super(getErrorMessage(code, optional), e);
    }

    public WalkException(byte code, @Nullable Exception e) {
        this(code, null, e);
    }
}
