package fr.heriamc.games.engine.map.slime;

import fr.heriamc.games.engine.map.Map;
import org.bukkit.World;

public interface SlimeMap extends Map {

    World getWorld();

    SlimeMap setWorld(World world);

}