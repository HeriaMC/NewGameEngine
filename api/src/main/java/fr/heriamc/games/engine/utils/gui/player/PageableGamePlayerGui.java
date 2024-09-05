package fr.heriamc.games.engine.utils.gui.player;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.gui.game.PageableGameGui;
import lombok.Getter;

@Getter
public abstract class PageableGamePlayerGui<M extends MiniGame, G extends BaseGamePlayer, E> extends PageableGameGui<M, E> {

    private final G gamePlayer;

    public PageableGamePlayerGui(M game, G gamePlayer, String inventoryName, boolean updatable, int rows, int maxItems) {
        super(game, inventoryName, updatable, rows, maxItems);
        this.gamePlayer = gamePlayer;
    }

}