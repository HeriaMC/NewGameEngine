package fr.heriamc.games.engine.utils;

import fr.heriamc.games.engine.utils.collection.SizedHashSet;
import fr.heriamc.games.engine.utils.collection.SizedSet;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class MaterialUtils {

    private final SizedSet<Material> interactableMaterials = new SizedHashSet<>(
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.FURNACE,
            Material.DISPENSER,
            Material.HOPPER,
            Material.BEACON,
            Material.ANVIL,
            Material.ENDER_CHEST,
            Material.ENCHANTMENT_TABLE,
            Material.WORKBENCH,
            Material.JUKEBOX,
            Material.LEVER,
            Material.STONE_BUTTON,
            Material.WOOD_BUTTON,
            Material.TRAP_DOOR,
            Material.DIODE_BLOCK_OFF,
            Material.DIODE_BLOCK_ON,
            Material.REDSTONE_COMPARATOR,
            Material.REDSTONE_TORCH_OFF,
            Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON,
            Material.PISTON_BASE,
            Material.PISTON_STICKY_BASE,
            Material.PISTON_MOVING_PIECE,
            Material.BREWING_STAND,
            Material.CAULDRON,
            Material.SIGN_POST,
            Material.WALL_SIGN,
            Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.ACACIA_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.SPRUCE_DOOR,
            Material.DARK_OAK_DOOR,
            Material.DAYLIGHT_DETECTOR,
            Material.DAYLIGHT_DETECTOR_INVERTED
    );

    public boolean isInteractable(Material material) {
        return interactableMaterials.contains(material);
    }

}