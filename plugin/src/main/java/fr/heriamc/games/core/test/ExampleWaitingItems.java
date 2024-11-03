package fr.heriamc.games.core.test;

import fr.heriamc.games.engine.waitingroom.WaitingRoomItems;
import org.bukkit.inventory.ItemStack;

public enum ExampleWaitingItems implements WaitingRoomItems {
    PLAY ();

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

}