package net.tracystacktrace.mamasrecipes.crawler;

import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ICrawler {

    @Nullable List<IRecipeDescription> getRecipes();

    default void initializeRecipes(@NotNull IEnvironment environment) {
        if (this.getRecipes() != null) {
            for (IRecipeDescription recipe : this.getRecipes()) {
                environment.addRecipe(recipe);
            }
        }
    }
}
