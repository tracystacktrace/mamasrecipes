package net.tracystacktrace.mamasrecipes.constructor.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.constructor.ItemDescription;
import net.tracystacktrace.mamasrecipes.tools.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.tools.SafeExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeFurnace implements IRecipeDescription {
    protected final ItemDescription input;
    protected final ItemDescription output;
    protected final String type;
    protected String name;

    public RecipeFurnace(
            ItemDescription input,
            ItemDescription output,
            String type
    ) {
        this.input = input;
        this.output = output;
        this.type = type;
    }

    public @NotNull ItemDescription getInput() {
        return this.input;
    }

    @Override
    public @NotNull String getType() {
        return this.type;
    }

    @Override
    public @NotNull ItemDescription getResult() {
        return this.output;
    }

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public @Nullable String getName() {
        return this.name;
    }

    public static @NotNull RecipeFurnace fromJson(@NotNull JsonObject object, @Nullable String type) throws RecipeProcessException {
         if(type == null || type.isEmpty()) {
             type = "furnace"; //dumb protection
         }

        //general checks
        if(!SafeExtractor.assertRecipeType(object, type)) {
            throw new RecipeProcessException(RecipeProcessException.INCORRECT_RECIPE_TYPE, object.has("type") ? object.get("type").toString() : null);
        }

        //build input item
        ItemDescription build_input = null;

        if(object.has("input")) {
            final JsonElement inputElement = object.get("input");
            if(!inputElement.isJsonObject()) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_RESULT, inputElement.toString());
            }

            try {
                build_input = ItemDescription.fromJson(inputElement.getAsJsonObject());
            } catch (RecipeProcessException e) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_RESULT, inputElement.toString(), e);
            }
        }

        if(build_input == null) {
            throw new RecipeProcessException(RecipeProcessException.RECIPE_RESULT_NOT_FOUND);
        }

        //build result item
        ItemDescription build_result = null;

        if(object.has("result")) {
            final JsonElement resultElement = object.get("result");
            if(!resultElement.isJsonObject()) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_RESULT, resultElement.toString());
            }

            try {
                build_result = ItemDescription.fromJson(resultElement.getAsJsonObject());
            } catch (RecipeProcessException e) {
                throw new RecipeProcessException(RecipeProcessException.INVALID_RECIPE_RESULT, resultElement.toString(), e);
            }
        }

        if(build_result == null) {
            throw new RecipeProcessException(RecipeProcessException.RECIPE_RESULT_NOT_FOUND);
        }

        final RecipeFurnace instance = new RecipeFurnace(build_input, build_result, type);

        //additional support for names, just for fun!
        if(object.has("name")) {
            final JsonElement nameElement = object.get("name");
            final String nameExtracted = SafeExtractor.extractString(nameElement);

            if(nameExtracted != null) {
                instance.name = nameExtracted;
            }
        }

        return instance;
    }
}
