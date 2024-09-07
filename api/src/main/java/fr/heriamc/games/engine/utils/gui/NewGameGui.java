package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.engine.Game;
import fr.heriamc.games.engine.player.GamePlayer;
import fr.heriamc.games.engine.team.GameTeam;
import org.bukkit.event.inventory.InventoryType;

public abstract class NewGameGui<M extends Game<G, T, ?>, G extends GamePlayer<T>, T extends GameTeam<G>> extends HeriaMenu {

    protected final M game;
    protected final G gamePlayer;

    public NewGameGui(M game, G gamePlayer, String name, int size, boolean update) {
        super(gamePlayer.getPlayer(), name, size, update);
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

    public NewGameGui(M game, G gamePlayer, String name, InventoryType type, boolean update) {
        super(gamePlayer.getPlayer(), name, type, update);
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

    public T getTeam() {
        return gamePlayer.getTeam();
    }

}