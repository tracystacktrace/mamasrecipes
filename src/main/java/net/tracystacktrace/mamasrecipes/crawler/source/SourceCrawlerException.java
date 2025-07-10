package net.tracystacktrace.mamasrecipes.crawler.source;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SourceCrawlerException extends Exception {
    public static final byte FILE_READ_FAILED = -128;
    public static final byte INVALID_JSON_FILE = -127;
    public static final byte FILES_ARRAY_NOT_PRESENT = -126;
    public static final byte RECIPE_READ_FAILED = -125;

    public static @NotNull String getErrorMessage(byte code, @Nullable String optional) {
        switch (code) {
            case FILE_READ_FAILED: return String.format("Failed to read file: %s", optional);
            case INVALID_JSON_FILE: return String.format("Invalid json file: %s", optional);
            case FILES_ARRAY_NOT_PRESENT: return String.format("The parameter \"files\" is not present in file: %s", optional);
            case RECIPE_READ_FAILED: return String.format("Failed to read a recipe: %s", optional);

            default: return "Unknown error!";
        }
    }

    public SourceCrawlerException(byte code, @Nullable String optional, @Nullable Exception e) {
        super(getErrorMessage(code, optional), e);
    }

    public SourceCrawlerException(byte code, @Nullable Exception e) {
        this(code, null, e);
    }
}
