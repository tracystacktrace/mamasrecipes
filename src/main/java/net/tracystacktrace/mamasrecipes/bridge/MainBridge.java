package net.tracystacktrace.mamasrecipes.bridge;

import net.tracystacktrace.mamasrecipes.tools.ISource;
import org.jetbrains.annotations.NotNull;

public final class MainBridge {
    private static ILocalization localization;
    private static ISource mainstreamSource;

    public static void setLocalization(@NotNull ILocalization localization) {
        MainBridge.localization = localization;
    }

    public static void setMainstreamSource(@NotNull ISource source) {
        MainBridge.mainstreamSource = source;
    }

    public static @NotNull ILocalization getLocalization() {
        if (localization == null) {
            throw new RuntimeException("Localization is not initialized!");
        }
        return localization;
    }

    public static @NotNull ISource getMainstreamSource() {
        if (mainstreamSource == null) {
            throw new RuntimeException("Mainstream ISource is not present!");
        }
        return mainstreamSource;
    }
}
