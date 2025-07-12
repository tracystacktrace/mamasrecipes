package net.tracystacktrace.mamasrecipes.bridge;

import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.item.ItemDescription;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEnvironment {
    /* general handlers */

    @Nullable Integer getItemIDFromName(@NotNull String name);

    boolean isValidItemID(int id);

    /* specific attributes */

    String[] getCustomItemAttributes();

    void processCustomItemAttribute(
            @NotNull ItemDescription target,
            @NotNull String attribute,
            @Nullable Object value
    );

    /* recipes stuff */

    void addRecipe(@NotNull IRecipeDescription recipe);

    String[] getCustomRecipeTypes();

    IRecipeDescription processCustomRecipe(
            @NotNull String type,
            @NotNull JsonObject object
    ) throws RecipeProcessException;
}
