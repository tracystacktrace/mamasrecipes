package net.tracystacktrace.mamasrecipes.constructor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.tracystacktrace.mamasrecipes.bridge.ILocalization;
import net.tracystacktrace.mamasrecipes.bridge.MainBridge;
import net.tracystacktrace.mamasrecipes.tools.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.tools.SafeExtractor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class ItemDescription {
    protected final int itemID;
    protected final int count;
    protected final int meta;
    protected String displayName;

    protected ItemDescription(int itemID, int count, int meta) {
        this.itemID = itemID;
        this.count = count;
        this.meta = meta;
    }

    public int getItemID() {
        return this.itemID;
    }

    public int getMeta() {
        return this.meta;
    }

    public int getCount() {
        return this.count;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static @NotNull ItemDescription fromJson(@NotNull JsonObject object) throws RecipeProcessException {
        final ILocalization localizer = MainBridge.getLocalization();

        //process item id/identifier
        int build_itemID = Integer.MIN_VALUE;

        if (object.has("item")) {
            final JsonElement itemElement = object.get("item");
            if (itemElement.isJsonPrimitive()) {
                final JsonPrimitive itemPrimitive = itemElement.getAsJsonPrimitive();
                if (itemPrimitive.isString()) {
                    final String value_iid = itemPrimitive.getAsString();
                    final Integer processed_iid = localizer.getIDFromName(value_iid);

                    if (processed_iid != null) {
                        build_itemID = processed_iid;
                    } else {
                        throw new RecipeProcessException(RecipeProcessException.INVALID_ITEM_ID, value_iid);
                    }
                } else if (itemPrimitive.isNumber()) {
                    final int value_iid = itemPrimitive.getAsInt();

                    if (localizer.isValidItemID(value_iid)) {
                        build_itemID = value_iid;
                    } else {
                        throw new RecipeProcessException(RecipeProcessException.INVALID_ITEM_ID, String.valueOf(value_iid));
                    }
                }
            }
        }

        if (build_itemID == Integer.MIN_VALUE) {
            throw new RecipeProcessException(RecipeProcessException.ITEM_IDENTIFIER_NOT_FOUND);
        }

        //process item meta
        int build_meta = 0;

        if (object.has("meta")) {
            final JsonElement metaElement = object.get("meta");
            final Integer metaExtracted = SafeExtractor.extractInt(metaElement);

            if (metaExtracted != null) {
                build_meta = metaExtracted;
            } else {
                throw new RecipeProcessException(RecipeProcessException.INVALID_ITEM_META, metaElement.toString());
            }
        }

        //process item count
        int build_count = 1;

        if (object.has("count")) {
            final JsonElement countElement = object.get("count");
            final Integer countExtracted = SafeExtractor.extractInt(countElement);

            if (countExtracted != null) {
                build_count = countExtracted;
            } else {
                throw new RecipeProcessException(RecipeProcessException.INVALID_ITEM_COUNT, countElement.toString());
            }
        }

        if (build_count < 1) {
            throw new RecipeProcessException(RecipeProcessException.INVALID_ITEM_COUNT, String.valueOf(build_count));
        }

        //generate item instance
        final ItemDescription instance = new ItemDescription(build_itemID, build_count, build_meta);

        //additional support for ReIndev's displayName
        if (object.has("displayName") && localizer.supportsAttribute("displayName")) {
            final JsonElement dnElement = object.get("displayName");
            final String dnExtracted = SafeExtractor.extractString(dnElement);

            if(dnExtracted != null) {
                instance.displayName = dnExtracted;
            } else {
                throw new RecipeProcessException(RecipeProcessException.INVALID_ITEM_DISPLAY_NAME, dnElement.toString());
            }
        }

        return instance;
    }
}
