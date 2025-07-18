package net.tracystacktrace.mamasrecipes.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.tracystacktrace.mamasrecipes.crawler.CrawlerException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public final class IOTools {
    public static @NotNull JsonObject readJSON(
            @NotNull Path file
    ) throws CrawlerException {
        String data;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {
            data = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new CrawlerException(CrawlerException.FILE_READ_FAILED, file.toFile().getAbsolutePath(), e);
        }

        try {
            return JsonParser.parseString(data).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new CrawlerException(CrawlerException.INVALID_JSON_FILE, file.toFile().getAbsolutePath(), e);
        }
    }

    public static @NotNull JsonObject readJSON(
            @NotNull InputStream inputStream,
            @NotNull String debugName
    ) throws CrawlerException {
        String data;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            data = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new CrawlerException(CrawlerException.FILE_READ_FAILED, debugName, e);
        }

        try {
            return JsonParser.parseString(data).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new CrawlerException(CrawlerException.INVALID_JSON_FILE, debugName, e);
        }
    }
}
