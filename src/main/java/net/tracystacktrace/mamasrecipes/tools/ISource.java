package net.tracystacktrace.mamasrecipes.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public interface ISource {
    @NotNull String getSourceName();

    boolean hasFile(@NotNull String path);

    @Nullable InputStream getStream(@NotNull String path);
}
