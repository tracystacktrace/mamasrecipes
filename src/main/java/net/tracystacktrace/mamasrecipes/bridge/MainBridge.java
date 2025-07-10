package net.tracystacktrace.mamasrecipes.bridge;

import org.jetbrains.annotations.NotNull;

public final class MainBridge {
    private static ILocalization localization;

    public static void setLocalization(@NotNull ILocalization localization) {
        MainBridge.localization = localization;
    }

    public static @NotNull ILocalization getLocalization() {
        if (localization == null) {
            throw new RuntimeException("Localization is not initialized!");
        }
        return localization;
    }
}
