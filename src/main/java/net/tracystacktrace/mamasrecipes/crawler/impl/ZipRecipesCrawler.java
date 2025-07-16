package net.tracystacktrace.mamasrecipes.crawler.impl;

import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
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
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipRecipesCrawler implements ICrawler {
    protected final String zipFileName;
    protected final List<IRecipeDescription> recipes;

    public ZipRecipesCrawler(String zipFileName, List<IRecipeDescription> recipes) {
        this.zipFileName = zipFileName;
        this.recipes = recipes;
    }

    @Override
    public @Nullable List<IRecipeDescription> getRecipes() {
        return this.recipes;
    }

    public @NotNull String getZipFileName() {
        return this.zipFileName;
    }

    public static @NotNull ZipRecipesCrawler fromZip(
            @NotNull IEnvironment environment,
            @NotNull ZipFile zipFile
    ) throws CrawlerException {
        final Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        final List<IRecipeDescription> collector = new ArrayList<>();

        while (enumeration.hasMoreElements()) {
            final ZipEntry entry = enumeration.nextElement();
            if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".json")) {
                final JsonObject object = readEntryAsJson(zipFile, entry);
                try {
                    final IRecipeDescription candidate_instance = RecipeReader.read(environment, object);
                    collector.add(candidate_instance);
                } catch (RecipeProcessException e) {
                    throw new CrawlerException(CrawlerException.RECIPE_PROCESS_FAILED, getDebugString(zipFile, entry), e);
                }
            }
        }

        System.out.println(String.format("Collected about %s recipes inside the folder: %s", collector.size(), zipFile.getName()));

        return new ZipRecipesCrawler(zipFile.getName(), collector);
    }

    static @NotNull JsonObject readEntryAsJson(@NotNull ZipFile zipFile, @NotNull ZipEntry entry) throws CrawlerException {
        try (InputStream stream = zipFile.getInputStream(entry)) {
            return IOTools.readJSON(stream, entry.getName());
        } catch (IOException e) {
            throw new CrawlerException(CrawlerException.FILE_READ_FAILED, getDebugString(zipFile, entry), e);
        }
    }

    static @NotNull String getDebugString(@NotNull ZipFile zipFile, @NotNull ZipEntry entry) {
        return String.format("%s:%s", zipFile.getName(), entry.getName());
    }
}
