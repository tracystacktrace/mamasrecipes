package net.tracystacktrace.mamasrecipes.bridge;

import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ILocalization {
    /* general handlers */

    @Nullable Integer getIDFromName(@NotNull String name);

    boolean isValidItemID(int id);

    /* specific handlers */

    String[] getAvailableTypes();

    void addRecipe(@NotNull IRecipeDescription recipe);

    /* specific attributes */
    boolean supportsAttribute(@NotNull String attribute);
}
