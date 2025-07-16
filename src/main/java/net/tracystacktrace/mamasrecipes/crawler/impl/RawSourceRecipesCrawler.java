package net.tracystacktrace.mamasrecipes.crawler.impl;

import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.bridge.ISource;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.RecipeReader;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import net.tracystacktrace.mamasrecipes.crawler.CrawlerException;
import net.tracystacktrace.mamasrecipes.crawler.ICrawler;
import net.tracystacktrace.mamasrecipes.tools.IOTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RawSourceRecipesCrawler implements ICrawler {
    protected final String texturepackName;
    protected final List<IRecipeDescription> recipes;

    public RawSourceRecipesCrawler(String texturepackName, List<IRecipeDescription> recipes) {
        this.texturepackName = texturepackName;
        this.recipes = recipes;
    }

    @Override
    public @Nullable List<IRecipeDescription> getRecipes() {
        return this.recipes;
    }

    public @NotNull String getSourceName() {
        return this.texturepackName;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static @NotNull RawSourceRecipesCrawler fromSource(
            @NotNull IEnvironment environment,
            @NotNull ISource source,
            @NotNull String @NotNull [] recipesPaths
    ) throws CrawlerException {
        final List<IRecipeDescription> collector = new ArrayList<>();

        for (int i = 0; i < recipesPaths.length; i++) {
            String candidate_path = recipesPaths[i];
            if (!candidate_path.startsWith("/")) {
                candidate_path = "/" + candidate_path;
            }

            if (source.hasFile(candidate_path)) {
                final InputStream stream1 = source.getStream(candidate_path);

                if (stream1 != null) {
                    final JsonObject candidate_json = IOTools.readJSON(stream1, candidate_path);
                    try {
                        final IRecipeDescription candidate_instance = RecipeReader.read(environment, candidate_json);
                        collector.add(candidate_instance);
                    } catch (RecipeProcessException e) {
                        throw new CrawlerException(CrawlerException.RECIPE_PROCESS_FAILED, candidate_path, e);
                    }
                }

                close(stream1);
            }
        }

        System.out.println(String.format("Collected about %s recipes in stream source: %s", collector.size(), source.getSourceName()));

        return new RawSourceRecipesCrawler(source.getSourceName(), collector);
    }

    static void close(@Nullable InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignored) {
            }
        }
    }
}
