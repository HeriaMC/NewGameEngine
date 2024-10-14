package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.bukkit.menu.pagination.HeriaPaginationMenu;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;

import java.util.List;
import java.util.function.Supplier;

public abstract class BaseGamePageableGui<M extends MiniGame, G extends BaseGamePlayer, E> extends HeriaPaginationMenu<E> {

    protected final M game;
    protected final G gamePlayer;

    public BaseGamePageableGui(M game, G gamePlayer, String name, int size, boolean update, List<Integer> slots, Supplier<List<E>> items) {
        super(gamePlayer.getPlayer(), name, size, update, slots, items);
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

}