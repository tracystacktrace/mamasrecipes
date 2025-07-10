package net.tracystacktrace.mamasrecipes.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SafeExtractor {
    public static @Nullable String extractString(@NotNull JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isString()) {
                return element.getAsString();
            }
        }
        return null;
    }

    public static @Nullable Integer extractInt(@NotNull JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isNumber()) {
                return element.getAsInt();
            }
        }
        return null;
    }

    public static @Nullable String[] extractStringArray(@NotNull JsonElement element) {
        if (element.isJsonArray()) {
            final List<String> collector = new ArrayList<>();
            for (JsonElement candidate : element.getAsJsonArray()) {
                if (candidate.isJsonPrimitive() && candidate.getAsJsonPrimitive().isString()) {
                    collector.add(candidate.getAsString());
                }
            }
            return collector.isEmpty() ? null : collector.toArray(new String[0]);
        }
        return null;
    }

    public static boolean assertRecipeType(@NotNull JsonObject object, @NotNull String expected) {
        if (object.has("type")) {
            final JsonElement typeElement = object.get("type");
            return expected.equals(SafeExtractor.extractString(typeElement));
        }
        return false;
    }
}
