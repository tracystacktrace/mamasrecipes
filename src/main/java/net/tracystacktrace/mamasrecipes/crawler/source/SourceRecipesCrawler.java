package net.tracystacktrace.mamasrecipes.crawler.source;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.RecipeReader;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import net.tracystacktrace.mamasrecipes.crawler.ICrawler;
import net.tracystacktrace.mamasrecipes.tools.ISource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SourceRecipesCrawler implements ICrawler {
    protected final String texturepackName;
    protected final List<IRecipeDescription> recipes;

    public SourceRecipesCrawler(String texturepackName, List<IRecipeDescription> recipes) {
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

    public static @Nullable SourceRecipesCrawler fromSource(@NotNull ISource source) throws SourceCrawlerException {
        if(!source.hasFile("/mamasrecipes.json")) {
            return null;
        }

        final InputStream dictateJsonIS = source.getStream("/mamasrecipes.json");

        if(dictateJsonIS == null) {
            throw new SourceCrawlerException(SourceCrawlerException.FILE_READ_FAILED, "/mamasrecipes.json", null);
        }

        final JsonObject dictateContent = readJsonFile(dictateJsonIS, "/mamasrecipes.json");

        close(dictateJsonIS);

        final List<String> filesList = obtainRecipePaths(dictateContent, "/mamasrecipes.json");

        if(filesList.isEmpty()) {
            return null;
        }

        final List<IRecipeDescription> collector = new ArrayList<>();

        for(String candidate_path : filesList) {
            if(!candidate_path.startsWith("/")) {
                candidate_path = "/" + candidate_path;
            }

            if(source.hasFile(candidate_path)) {
                final InputStream stream1 = source.getStream(candidate_path);

                if(stream1 != null) {
                    final JsonObject candidate_json = readJsonFile(stream1, candidate_path);
                    try {
                        final IRecipeDescription candidate_instance = RecipeReader.read(candidate_json);
                        collector.add(candidate_instance);
                    } catch (RecipeProcessException e) {
                        throw new SourceCrawlerException(SourceCrawlerException.RECIPE_READ_FAILED, candidate_path, e);
                    }
                }

                close(stream1);
            }
        }

        System.out.println("Collected about " + collector.size() + " recipes in stream source: " + source.getSourceName());

        return new SourceRecipesCrawler(source.getSourceName(), collector);
    }


    static List<String> obtainRecipePaths(@NotNull JsonObject object, @NotNull String debugName) throws SourceCrawlerException {
        if(!object.has("files")) {
            throw new SourceCrawlerException(SourceCrawlerException.FILES_ARRAY_NOT_PRESENT, debugName, null);
        }

        final JsonElement filesElement = object.get("files");

        if(!filesElement.isJsonArray()) {
            throw new SourceCrawlerException(SourceCrawlerException.INVALID_JSON_FILE, filesElement.toString(), null);
        }

        final List<String> collector = new ArrayList<>();

        for(JsonElement candidate : filesElement.getAsJsonArray()) {
            if(candidate.isJsonPrimitive() && candidate.getAsJsonPrimitive().isString()) {
                collector.add(candidate.getAsString());
            }
        }

        return collector;
    }

    static @NotNull JsonObject readJsonFile(
            @NotNull InputStream inputStream,
            @NotNull String debugName
    ) throws SourceCrawlerException {
        String data;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            data = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new SourceCrawlerException(SourceCrawlerException.FILE_READ_FAILED, debugName, e);
        }

        try {
            return JsonParser.parseString(data).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new SourceCrawlerException(SourceCrawlerException.INVALID_JSON_FILE, debugName, e);
        }
    }

    static void close(@Nullable InputStream stream) {
        if(stream != null) {
            try {
                stream.close();
            } catch (IOException ignored) {}
        }
    }
}
