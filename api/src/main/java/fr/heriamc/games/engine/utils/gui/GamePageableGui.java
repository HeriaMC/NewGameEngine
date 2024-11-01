package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.menu.pagination.HeriaPaginationMenu;
import fr.heriamc.games.engine.Game;
import fr.heriamc.games.engine.player.GamePlayer;
import fr.heriamc.games.engine.team.GameTeam;

import java.util.List;
import java.util.function.Supplier;

public abstract class GamePageableGui<M extends Game<G, T, ?>, G extends GamePlayer<T>, T extends GameTeam<G>, E> extends HeriaPaginationMenu<E> {

    protected final M game;
    protected final G gamePlayer;

    public GamePageableGui(M game, G gamePlayer, String name, int size, boolean update, List<Integer> slots, Supplier<List<E>> items) {
        super(gamePlayer.getPlayer(), name, size, update, slots, items);
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

    protected void openGui(HeriaMenu heriaMenu) {
        HeriaBukkit.get().getMenuManager().open(heriaMenu);
    }

}