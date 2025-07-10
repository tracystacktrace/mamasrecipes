package net.tracystacktrace.mamasrecipes.walk;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.tracystacktrace.mamasrecipes.bridge.ILocalization;
import net.tracystacktrace.mamasrecipes.bridge.MainBridge;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.RecipeReader;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WalkFolder {
    protected final File folder;
    protected final List<IRecipeDescription> recipes;

    public WalkFolder(File folder, List<IRecipeDescription> recipes) {
        this.folder = folder;
        this.recipes = recipes;
    }

    public void initializeRecipes() {
        final ILocalization localized = MainBridge.getLocalization();
        if(this.recipes != null) {
            for(IRecipeDescription recipe : this.recipes) {
                localized.addRecipe(recipe);
            }
        }
    }

    public @NotNull File getFolder() {
        return this.folder;
    }

    public static @NotNull WalkFolder walk(@NotNull File file) throws WalkException {
        if(!file.exists()) {
            file.mkdirs();
        }

        if(!file.isDirectory()) {
            throw new WalkException(WalkException.NOT_A_FOLDER, file.getAbsolutePath(), null);
        }

        final List<Path> folderResult = collectJsonFiles(file);

        if(folderResult.isEmpty()) {
            System.out.printf("The folder is empty, ignoring: %s", file.getAbsolutePath());
            return new WalkFolder(file, null);
        }

        List<IRecipeDescription> collector = new ArrayList<>();
        for(Path path : folderResult) {
            final JsonObject content = readJsonFile(path);
            try {
                final IRecipeDescription description = RecipeReader.read(content);
                collector.add(description);
            } catch (RecipeProcessException e) {
                throw new WalkException(WalkException.RECIPE_PROCESS_FAILED, path.toFile().getAbsolutePath(), e);
            }
        }

        System.out.println("Collected about " + collector.size() + " recipes in folder: " + file.getAbsolutePath());

        return new WalkFolder(file, collector);
    }

    static @NotNull List<@NotNull Path> collectJsonFiles(@NotNull File folder) throws WalkException {
        try (Stream<Path> paths = Files.walk(folder.toPath())) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new WalkException(WalkException.FOLDER_READ_FAILED, folder.getAbsolutePath(), e);
        }
    }

    static @NotNull JsonObject readJsonFile(@NotNull Path file) throws WalkException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new WalkException(WalkException.FILE_READ_FAILED, file.toFile().getAbsolutePath(), e);
        }

        try {
            return JsonParser.parseString(builder.toString()).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new WalkException(WalkException.INVALID_JSON_FILE, file.toFile().getAbsolutePath(), e);
        }
    }
}
