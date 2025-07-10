package net.tracystacktrace.mamasrecipes.bridge;

import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface ILocalization {
    /* general handlers */

    @Nullable Integer getIDFromName(@NotNull String name);

    boolean isValidItemID(int id);

    /* specific handlers */

    void addRecipe(@NotNull IRecipeDescription recipe);

    /* specific attributes */

    boolean supportsAttribute(@NotNull String attribute);

    String[] getCustomRecipeTypes();

    IRecipeDescription processCustomRecipe(JsonObject object) throws RecipeProcessException;
}
