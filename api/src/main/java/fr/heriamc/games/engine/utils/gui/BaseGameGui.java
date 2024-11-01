package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public abstract class BaseGameGui<M extends MiniGame, G extends BaseGamePlayer> extends HeriaMenu {

    protected final M game;
    protected final G gamePlayer;

    protected final HeriaMenu beforeMenu;

    public BaseGameGui(M game, G gamePlayer, HeriaMenu beforeMenu, String name, int size, boolean update) {
        super(gamePlayer.getPlayer(), name, size, update);
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.beforeMenu = beforeMenu;
    }

    public BaseGameGui(M game, G gamePlayer, HeriaMenu beforeMenu, String name, InventoryType type, boolean update) {
        super(gamePlayer.getPlayer(), name, type, update);
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.beforeMenu = beforeMenu;
    }

    protected void closeOrOpenBefore(InventoryClickEvent event) {
        if (beforeMenu == null) gamePlayer.closeInventory();
        else HeriaBukkit.get().getMenuManager().open(beforeMenu);
    }

    protected void openGui(HeriaMenu heriaMenu) {
        HeriaBukkit.get().getMenuManager().open(heriaMenu);
    }

}