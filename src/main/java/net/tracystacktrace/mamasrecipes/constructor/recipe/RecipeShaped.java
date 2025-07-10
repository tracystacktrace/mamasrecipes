package net.tracystacktrace.mamasrecipes.constructor.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.constructor.item.ItemDescription;
import net.tracystacktrace.mamasrecipes.constructor.item.KeyedItemDescription;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.tools.SafeExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RecipeShaped implements IRecipeDescription {
    protected final String[] pattern;
    protected final Map<String, KeyedItemDescription> keys;
    protected final ItemDescription resultItem;
    protected String name;

    protected RecipeShaped(
            @NotNull String[] pattern,
            @NotNull Map<String, KeyedItemDescription> keys,
            @NotNull ItemDescription resultItem
    ) {
        this.pattern = pattern;
        this.keys = keys;
        this.resultItem = resultItem;
    }

    public @NotNull String @NotNull [] getPattern() {
        return this.pattern;
    }

    public @NotNull Map<String, KeyedItemDescription> getKeys() {
        return this.keys;
    }

    @Override
    public @NotNull String getType() {
        return "crafting_shaped";
    }

    @Override
    public @NotNull ItemDescription getResult() {
        return this.resultItem;
    }

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public @Nullable String getName() {
        return this.name;
    }

    public static @NotNull RecipeShaped fromJson(@NotNull JsonObject object) throws RecipeProcessException {
        //general checks
        if (!SafeExtractor.assertRecipeType(object, "crafting_shaped")) {
            throw new RecipeProcessException(RecipeProcessException.INCORRECT_RECIPE_TYPE, object.has("type") ? object.get("type").toString() : null);
        }

        //build pattern part
        String[] build_pattern = null;

        if (object.has("pattern")) {
            final JsonElement patternElement = object.get("pattern");
            final String[] patternExtracted = SafeExtractor.extractStringArray(patternElement);
            if (patternExtracted != null) {
                build_pattern = patternExtracted;
            } else {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_PATTERN, patternElement.toString());
            }
        }

        if (build_pattern == null) {
            throw new RecipeProcessException(RecipeProcessException.RECIPE_PATTERN_NOT_FOUND);
        }

        //build key list
        Map<String, KeyedItemDescription> build_keys = null;

        if (object.has("keys")) {
            final JsonElement keysElement = object.get("keys");

            if (!keysElement.isJsonArray()) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEYS, keysElement.toString());
            }

            final Map<String, KeyedItemDescription> collector = new HashMap<>();

            for (JsonElement candidateItem : keysElement.getAsJsonArray()) {
                if (!candidateItem.isJsonObject()) {
                    throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEY, candidateItem.toString());
                }

                try {
                    final KeyedItemDescription candidateInstance = KeyedItemDescription.fromJson(candidateItem.getAsJsonObject());
                    collector.put(candidateInstance.getKey(), candidateInstance);
                } catch (RecipeProcessException e) {
                    throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEY, candidateItem.toString(), e);
                }
            }

            build_keys = collector.isEmpty() ? null : collector;
        }

        if (build_keys == null) {
            throw new RecipeProcessException(RecipeProcessException.RECIPE_KEYS_NOT_FOUND);
        }

        //build result item
        ItemDescription build_result = null;

        if (object.has("result")) {
            final JsonElement resultElement = object.get("result");
            if (!resultElement.isJsonObject()) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_RESULT, resultElement.toString());
            }

            try {
                build_result = ItemDescription.fromJson(resultElement.getAsJsonObject());
            } catch (RecipeProcessException e) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_RESULT, resultElement.toString(), e);
            }
        }

        if (build_result == null) {
            throw new RecipeProcessException(RecipeProcessException.RECIPE_RESULT_NOT_FOUND);
        }

        //construct recipe instance
        final RecipeShaped instance = new RecipeShaped(build_pattern, build_keys, build_result);

        //additional support for names, just for fun!
        if (object.has("name")) {
            final JsonElement nameElement = object.get("name");
            final String nameExtracted = SafeExtractor.extractString(nameElement);

            if (nameExtracted != null) {
                instance.name = nameExtracted;
            }
        }

        return instance;
    }
}
