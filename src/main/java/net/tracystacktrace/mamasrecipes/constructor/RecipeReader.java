package net.tracystacktrace.mamasrecipes.constructor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeDirectProcessing;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeShaped;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeShapeless;
import net.tracystacktrace.mamasrecipes.tools.SafeExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RecipeReader {

    static boolean isValidType(@NotNull String check, @NotNull String @Nullable [] custom) {
        if (check.equals("crafting_shaped") || check.equals("crafting_shapeless") || check.equals("furnace")) {
            return true;
        }

        if (custom == null) {
            return false;
        }

        for (String a : custom) {
            if (check.equals(a)) {
                return true;
            }
        }
        return false;
    }

    public static @NotNull IRecipeDescription read(
            @NotNull IEnvironment environment,
            @NotNull JsonObject object
    ) throws RecipeProcessException {
        final String[] customTypes = environment.getCustomRecipeTypes();

        if (object.has("type")) {
            final JsonElement typeElement = object.get("type");
            final String typeExtracted = SafeExtractor.extractString(typeElement);

            if (typeExtracted == null || !isValidType(typeExtracted, customTypes)) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_TYPE, typeElement.toString());
            }

            switch (typeExtracted) {
                case "crafting_shaped": {
                    return RecipeShaped.fromJson(environment, object);
                }
                case "crafting_shapeless": {
                    return RecipeShapeless.fromJson(environment, object);
                }
                case "furnace": {
                    return RecipeDirectProcessing.fromJson(environment, object, "furnace");
                }
                default: {
                    final IRecipeDescription description = environment.processCustomRecipe(typeExtracted, object);
                    if (description == null) {
                        throw new RecipeProcessException(RecipeProcessException.INCORRECT_RECIPE_TYPE, typeExtracted);
                    }
                    return description;
                }
            }
        } else {
            throw new RecipeProcessException(RecipeProcessException.INCORRECT_RECIPE_TYPE, "null");
        }
    }

}
