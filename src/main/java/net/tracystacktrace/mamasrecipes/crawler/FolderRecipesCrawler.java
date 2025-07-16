package net.tracystacktrace.mamasrecipes.crawler;

import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.RecipeReader;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import net.tracystacktrace.mamasrecipes.tools.IOTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolderRecipesCrawler implements ICrawler {
    protected final File folder;
    protected final List<IRecipeDescription> recipes;

    public FolderRecipesCrawler(File folder, List<IRecipeDescription> recipes) {
        this.folder = folder;
        this.recipes = recipes;
    }

    @Override
    public @Nullable List<IRecipeDescription> getRecipes() {
        return this.recipes;
    }

    public @NotNull File getFolder() {
        return this.folder;
    }

    public static @Nullable FolderRecipesCrawler fromFolder(
            @NotNull IEnvironment environment,
            @NotNull File file
    ) throws CrawlerException {
        if (!file.exists()) {
            file.mkdirs();
        }

        if (!file.isDirectory()) {
            throw new CrawlerException(CrawlerException.NOT_A_FOLDER, file.getAbsolutePath(), null);
        }

        final List<Path> folderResult = collectJsonFiles(file);

        if (folderResult.isEmpty()) {
            System.out.println(String.format("The folder is empty, ignoring: %s", file.getAbsolutePath()));
            return null;
        }

        final List<IRecipeDescription> collector = new ArrayList<>();
        for (Path path : folderResult) {
            final JsonObject content = IOTools.readJSON(path);
            try {
                final IRecipeDescription description = RecipeReader.read(environment, content);
                collector.add(description);
            } catch (RecipeProcessException e) {
                throw new CrawlerException(CrawlerException.RECIPE_PROCESS_FAILED, path.toFile().getAbsolutePath(), e);
            }
        }

        System.out.println(String.format("Collected about %s recipes inside the folder: %s", collector.size(), file.getAbsolutePath()));

        return new FolderRecipesCrawler(file, collector);
    }

    static @NotNull List<@NotNull Path> collectJsonFiles(@NotNull File folder) throws CrawlerException {
        try (Stream<Path> paths = Files.walk(folder.toPath())) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new CrawlerException(CrawlerException.FOLDER_READ_FAILED, folder.getAbsolutePath(), e);
        }
    }
}
