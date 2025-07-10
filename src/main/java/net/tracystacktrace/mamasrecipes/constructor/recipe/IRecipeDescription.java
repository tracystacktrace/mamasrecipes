package net.tracystacktrace.mamasrecipes.constructor.recipe;

import net.tracystacktrace.mamasrecipes.constructor.ItemDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRecipeDescription {
    @NotNull String getType();

    @NotNull ItemDescription getResult();

    boolean hasName();

    @Nullable String getName();
}
