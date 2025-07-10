import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.tracystacktrace.mamasrecipes.bridge.MainBridge;
import net.tracystacktrace.mamasrecipes.constructor.RecipeProcessException;
import net.tracystacktrace.mamasrecipes.constructor.item.ItemDescription;
import net.tracystacktrace.mamasrecipes.constructor.item.KeyedItemDescription;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//this is a set of ugly tests
public class ItemDescriptionTest {

    @BeforeAll
    static void initLocalizer() {
        MainBridge.setLocalization(new FakeLocalized());
    }

    @Test
    void testSampleItem() {
        final String jsonString = "{\"item\": \"item.diamond_sword\", \"meta\": 5, \"count\": 32, \"displayName\": \"Hello Sigma!\"}";
        final JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        ItemDescription description;
        try {
            description = ItemDescription.fromJson(jsonObject);
        } catch (RecipeProcessException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(description);
        assertEquals(FakeLocalized.DIAMOND_SWORD, description.getItemID());
        assertEquals(5, description.getMeta());
        assertEquals(32, description.getCount());
        assertEquals("Hello Sigma!", description.getDisplayName());
    }

    @Test
    void testSampleKeyedItem() {
        final String jsonString = "{\"key\": \"X\", \"item\": \"item.coal\", \"meta\": 0, \"count\": 2, \"displayName\": \"Asdfgh\"}";
        final JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        KeyedItemDescription description;
        try {
            description = KeyedItemDescription.fromJson(jsonObject);
        } catch (RecipeProcessException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(description);
        assertEquals("X", description.getKey());
        assertEquals(FakeLocalized.COAL, description.getItemID());
        assertEquals(0, description.getMeta());
        assertEquals(2, description.getCount());
        assertEquals("Asdfgh", description.getDisplayName());
    }

}
