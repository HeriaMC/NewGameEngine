package fr.heriamc.games.engine.ability;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class GameAbilityItem<M extends MiniGame, E extends PlayerEvent> extends GameAbility<M, E> {

    private final ItemStack itemStack;

    public GameAbilityItem(String name, Class<M> gameClass, Class<E> eventClass, ItemStack itemStack) {
        super(name, gameClass, eventClass);
        this.itemStack = itemStack;
    }

    public void giveItem(Player player) {
        player.getInventory().addItem(itemStack);
    }

    public void giveItem(BaseGamePlayer gamePlayer) {
        giveItem(gamePlayer.getPlayer());
    }

    public void giveItem(int slot, Player player) {
        player.getInventory().setItem(slot, itemStack);
    }

    public void giveItem(int slot, BaseGamePlayer gamePlayer) {
        giveItem(slot, gamePlayer.getPlayer());
    }

}