package net.tracystacktrace.mamasrecipes.crawler;

import net.tracystacktrace.mamasrecipes.bridge.ILocalization;
import net.tracystacktrace.mamasrecipes.bridge.MainBridge;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ICrawler {

    @Nullable List<IRecipeDescription> getRecipes();

    default void initializeRecipes() {
        final ILocalization localized = MainBridge.getLocalization();
        if(this.getRecipes() != null) {
            for(IRecipeDescription recipe : this.getRecipes()) {
                localized.addRecipe(recipe);
            }
        }
    }
}
