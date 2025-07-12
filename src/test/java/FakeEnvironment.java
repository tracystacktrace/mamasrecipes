import com.google.gson.JsonObject;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.item.ItemDescription;
import net.tracystacktrace.mamasrecipes.constructor.recipe.IRecipeDescription;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeDirectProcessing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FakeEnvironment implements IEnvironment {
    public static final int DIAMOND_SWORD = 1;
    public static final int COAL = 2;
    public static final int STICK = 3;
    public static final int IRON_PICKAXE = 4;
    public static final int BLOCK_IRON = 5;

    @Override
    public @Nullable String getItemIDFromName(@NotNull String name, int meta, int count) {
        switch (name) {
            case "item.diamond_sword":
                return String.valueOf(DIAMOND_SWORD);
            case "item.coal":
                return String.valueOf(COAL);
            case "item.stick":
                return String.valueOf(STICK);
            case "item.iron_pickaxe":
                return String.valueOf(IRON_PICKAXE);
            case "block.iron":
                return String.valueOf(BLOCK_IRON);
        }
        return null;
    }

    @Override
    public @Nullable String getItemID(int id) {
        if(id < 1 || id >= 256) {
            return null;
        }
        return String.valueOf(id);
    }

    @Override
    public void addRecipe(@NotNull IRecipeDescription recipe) {
        System.out.println("Adding recipe: " + recipe.toString());
    }

    @Override
    public String[] getCustomItemAttributes() {
        return new String[]{"displayName"};
    }

    @Override
    public void processCustomItemAttribute(@NotNull ItemDescription target, @NotNull String attribute, @Nullable Object value) {
        if (attribute.equals("displayName")) {
            target.setDisplayName((value instanceof String) ? ((String) value) : String.valueOf(value));
        }
    }

    @Override
    public String[] getCustomRecipeTypes() {
        return new String[]{
                "skibidi", "sigma"
        };
    }

    @Override
    public IRecipeDescription processCustomRecipe(
            @NotNull String type,
            @NotNull JsonObject object
    ) throws RecipeProcessException {
        return RecipeDirectProcessing.fromJson(this, object, "skibidi");
    }
}
