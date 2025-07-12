package net.tracystacktrace.mamasrecipes.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean assertRecipeType(@NotNull JsonObject object, @NotNull String expected) {
        if (object.has("type")) {
            final JsonElement typeElement = object.get("type");
            return expected.equals(SafeExtractor.extractString(typeElement));
        }
        return false;
    }

    public static @Nullable Object extractSomething(@NotNull JsonElement element) {
        //null values
        if (element.isJsonNull()) {
            return null;
        }

        //primitives
        if (element.isJsonPrimitive()) {
            final JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString())
                return primitive.getAsString();
            if (primitive.isBoolean())
                return primitive.getAsBoolean();
            if (primitive.isNumber())
                return primitive.getAsNumber();
        }

        //json object
        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        }

        //json array
        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        }

        return null;
    }
}
