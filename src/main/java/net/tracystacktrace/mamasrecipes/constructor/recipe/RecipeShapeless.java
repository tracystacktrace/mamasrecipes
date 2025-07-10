package net.tracystacktrace.mamasrecipes.constructor.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.constructor.ItemDescription;
import net.tracystacktrace.mamasrecipes.tools.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.tools.SafeExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeShapeless implements IRecipeDescription {
    protected final ItemDescription resultItem;
    protected final ItemDescription[] keys;
    protected String name;

    public RecipeShapeless(
            ItemDescription resultItem,
            ItemDescription[] keys
    ) {
        this.resultItem = resultItem;
        this.keys = keys;
    }

    @Override
    public @NotNull String getType() {
        return "crafting_shapeless";
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


    public static @NotNull RecipeShapeless fromJson(@NotNull JsonObject object) throws RecipeProcessException {
        //general checks
        if (!SafeExtractor.assertRecipeType(object, "crafting_shapeless")) {
            throw new RecipeProcessException(RecipeProcessException.INCORRECT_RECIPE_TYPE, object.has("type") ? object.get("type").toString() : null);
        }

        //build key sey
        ItemDescription[] build_keys = null;

        if (object.has("keys")) {
            final JsonElement keysElement = object.get("keys");
            if (!keysElement.isJsonArray()) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEYS, keysElement.toString());
            }

            final List<ItemDescription> collector = new ArrayList<>();

            for (JsonElement candidate : keysElement.getAsJsonArray()) {
                if (!candidate.isJsonObject()) {
                    throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEY, candidate.toString());
                }

                try {
                    final ItemDescription buildCandidate = ItemDescription.fromJson(candidate.getAsJsonObject());
                    collector.add(buildCandidate);
                } catch (RecipeProcessException e) {
                    throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEY, candidate.toString(), e);
                }
            }

            build_keys = collector.isEmpty() ? null : collector.toArray(new ItemDescription[0]);
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

        //construct an instance
        final RecipeShapeless instance = new RecipeShapeless(build_result, build_keys);

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
