package net.tracystacktrace.mamasrecipes.bridge;

import org.jetbrains.annotations.NotNull;

public final class MainBridge {
    private static IEnvironment localization;

    public static void setLocalization(@NotNull IEnvironment localization) {
        MainBridge.localization = localization;
    }

    public static @NotNull IEnvironment getLocalization() {
        if (localization == null) {
            throw new RuntimeException("Localization is not initialized!");
        }
        return localization;
    }
}
