package net.tracystacktrace.mamasrecipes.constructor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.bridge.MainBridge;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeFurnace;
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
            @NotNull JsonObject object
    ) throws RecipeProcessException {
        final IEnvironment localized = MainBridge.getLocalization();
        final String[] customTypes = localized.getCustomRecipeTypes();

        if (object.has("type")) {
            final JsonElement typeElement = object.get("type");
            final String typeExtracted = SafeExtractor.extractString(typeElement);

            if (typeExtracted == null || !isValidType(typeExtracted, customTypes)) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_TYPE, typeElement.toString());
            }

            switch (typeExtracted) {
                case "crafting_shaped": {
                    return RecipeShaped.fromJson(object);
                }
                case "crafting_shapeless": {
                    return RecipeShapeless.fromJson(object);
                }
                case "furnace": {
                    return RecipeFurnace.fromJson(object, "furnace");
                }
                default: {
                    IRecipeDescription description = localized.processCustomRecipe(typeExtracted, object);
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
