package fr.heriamc.games.engine.utils.json.adapter;

import com.google.gson.*;
import fr.heriamc.games.engine.region.GameRegion;
import fr.heriamc.games.engine.utils.json.JsonObjectBuilder;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;

public class GameRegionAdapter implements JsonSerializer<GameRegion<?>>, JsonDeserializer<GameRegion<?>> {

    @Override
    public JsonElement serialize(GameRegion<?> region, Type type, JsonSerializationContext context) {
        return new JsonObjectBuilder()
                .add("name", region.getName())
                .add("world", region.getWorld().getName())
                .add("minX", region.getMinX())
                .add("minY", region.getMinY())
                .add("minZ", region.getMinZ())
                .add("maxX", region.getMaxX())
                .add("maxY", region.getMaxY())
                .add("maxZ", region.getMaxZ())
                .build();
    }

    @Override
    public GameRegion<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        final var jsonObject = jsonElement.getAsJsonObject();

        return new GameRegion<>(
                jsonObject.get("name").getAsString(),
                Bukkit.getWorld(jsonObject.get("world").getAsString()),
                jsonObject.get("minX").getAsDouble(),
                jsonObject.get("minY").getAsDouble(),
                jsonObject.get("minZ").getAsDouble(),
                jsonObject.get("maxX").getAsDouble(),
                jsonObject.get("maxY").getAsDouble(),
                jsonObject.get("maxZ").getAsDouble()
        );
    }

}