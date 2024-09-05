package fr.heriamc.games.engine.utils.newgui;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import org.bukkit.event.inventory.InventoryType;

public abstract class NewBaseGameGui<M extends MiniGame, G extends BaseGamePlayer> extends HeriaMenu {

    protected final M game;
    protected final G gamePlayer;

    public NewBaseGameGui(M game, G gamePlayer, String name, int size, boolean update) {
        super(gamePlayer.getPlayer(), name, size, update);
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

    public NewBaseGameGui(M game, G gamePlayer, String name, InventoryType type, boolean update) {
        super(gamePlayer.getPlayer(), name, type, update);
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

}