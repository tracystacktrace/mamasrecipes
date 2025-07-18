import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.tracystacktrace.mamasrecipes.bridge.IEnvironment;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeDirectProcessing;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeShaped;
import net.tracystacktrace.mamasrecipes.constructor.recipe.RecipeShapeless;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipesTest {
    private static final IEnvironment ENVIRONMENT = new FakeEnvironment();

    @Test
    void testRecipeShaped() {
        final String jsonString = "{\"type\":\"crafting_shaped\",\"name\":\"Special Recipe of my own!\",\"pattern\":[\"XX \",\"XXX\",\"LXX\"],\"keys\":[{\"key\":\"X\",\"item\":\"item.diamond_sword\"},{\"key\":\"L\",\"item\":\"item.stick\"}],\"result\":{\"item\":\"item.diamond_sword\",\"displayName\":\"Diamond Giant Sword!\",\"count\":1,\"meta\":0}}";
        final JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        RecipeShaped recipe;
        try {
            recipe = RecipeShaped.fromJson(ENVIRONMENT, jsonObject);
        } catch (RecipeProcessException e) {
            throw new RuntimeException(e);
        }

        //todo: complete keys test
        System.out.println(recipe.getKeys());

        assertNotNull(recipe);
        assertEquals("crafting_shaped", recipe.getType());
        assertEquals("Special Recipe of my own!", recipe.getName());

        //test pattern
        assertArrayEquals(new String[] {"XX ", "XXX", "LXX"}, recipe.getPattern());

        //item assert
        assertNotNull(recipe.getResult());
        assertEquals(String.valueOf(FakeEnvironment.DIAMOND_SWORD), recipe.getResult().getItemIdentifier());
        assertEquals("Diamond Giant Sword!", recipe.getResult().getDisplayName());
        assertEquals(1, recipe.getResult().getCount());
        assertEquals(0, recipe.getResult().getMeta());
    }

    @Test
    void testRecipeShapeless() {
        final String jsonString = "{\"type\":\"crafting_shapeless\",\"name\":\"Debug name line 23\",\"keys\":[{\"item\":\"block.iron\",\"meta\":3},{\"item\":\"item.stick\"}],\"result\":{\"item\":\"item.iron_pickaxe\",\"displayName\":\"Super Hammer\",\"count\":1,\"meta\":25}}";
        final JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        RecipeShapeless recipe;
        try {
            recipe = RecipeShapeless.fromJson(ENVIRONMENT, jsonObject);
        } catch (RecipeProcessException e) {
            throw new RuntimeException(e);
        }

        //assert default
        assertNotNull(recipe);
        assertEquals("crafting_shapeless", recipe.getType());
        assertEquals("Debug name line 23", recipe.getName());

        //assert keys (dumb way)
        assertNotNull(recipe.getInput());

        final String blockIron = String.valueOf(FakeEnvironment.BLOCK_IRON);
        final String stick = String.valueOf(FakeEnvironment.STICK);

        assertTrue(blockIron.equals(recipe.getInput()[0].getItemIdentifier()) || blockIron.equals(recipe.getInput()[1].getItemIdentifier()) , "Block iron detected in input keys!");
        assertTrue( stick.equals(recipe.getInput()[0].getItemIdentifier()) || stick.equals(recipe.getInput()[1].getItemIdentifier()), "Item stick detected in input keys!");

        //result assert
        assertNotNull(recipe.getResult());
        assertEquals(String.valueOf(FakeEnvironment.IRON_PICKAXE), recipe.getResult().getItemIdentifier());
        assertEquals("Super Hammer", recipe.getResult().getDisplayName());
        assertEquals(1, recipe.getResult().getCount());
        assertEquals(25, recipe.getResult().getMeta());
    }

    @Test
    void testRecipeFurnace() {
        final String jsonString = "{\"type\":\"furnace\",\"name\":\"Test 1\",\"input\":{\"item\":\"item.coal\",\"meta\":0},\"result\":{\"item\":\"item.coal\",\"displayName\":\"Super Coal\",\"meta\":1}}";
        final JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        RecipeDirectProcessing recipe;
        try {
            recipe = RecipeDirectProcessing.fromJson(ENVIRONMENT, jsonObject, "furnace");
        } catch (RecipeProcessException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(recipe);
        assertEquals("furnace", recipe.getType());
        assertEquals("Test 1", recipe.getName());

        //input assert
        assertNotNull(recipe.getInput());
        assertEquals(String.valueOf(FakeEnvironment.COAL), recipe.getInput().getItemIdentifier());
        assertNull(recipe.getInput().getDisplayName());
        assertEquals(1, recipe.getInput().getCount());
        assertEquals(0, recipe.getInput().getMeta());

        //result assert
        assertNotNull(recipe.getResult());
        assertEquals(String.valueOf(FakeEnvironment.COAL), recipe.getResult().getItemIdentifier());
        assertEquals("Super Coal", recipe.getResult().getDisplayName());
        assertEquals(1, recipe.getResult().getCount());
        assertEquals(1, recipe.getResult().getMeta());
    }

}
