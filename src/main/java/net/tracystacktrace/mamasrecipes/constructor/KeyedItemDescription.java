package net.tracystacktrace.mamasrecipes.constructor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.tools.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.tools.SafeExtractor;
import org.jetbrains.annotations.NotNull;

public class KeyedItemDescription extends ItemDescription {
    protected String recipeKeyIdentifier;

    protected KeyedItemDescription(ItemDescription description) {
        super(description.itemID, description.count, description.meta);
        this.displayName = description.displayName;
    }

    public @NotNull String getKey() {
        return this.recipeKeyIdentifier;
    }

    public static @NotNull KeyedItemDescription fromJson(@NotNull JsonObject object) throws RecipeProcessException {
        final KeyedItemDescription instance = new KeyedItemDescription(ItemDescription.fromJson(object));

        //additional support for key identifier
        if (object.has("key")) {
            final JsonElement keyElement = object.get("key");
            final String keyExtracted = SafeExtractor.extractString(keyElement);
            if(keyExtracted != null && !keyExtracted.isEmpty()) {
                instance.recipeKeyIdentifier = keyExtracted;
            } else {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_KEY, keyElement.toString());
            }
        }

        return instance;
    }
}
