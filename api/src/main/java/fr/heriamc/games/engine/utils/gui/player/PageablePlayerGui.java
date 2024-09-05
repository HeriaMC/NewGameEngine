package fr.heriamc.games.engine.utils.gui.player;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.gui.PageableGui;
import lombok.Getter;

@Getter
public abstract class PageablePlayerGui<G extends BaseGamePlayer, E> extends PageableGui<E> {

    private final G gamePlayer;

    public PageablePlayerGui(G gamePlayer, String inventoryName, int rows, int maxItems) {
        super(inventoryName, rows, maxItems);
        this.gamePlayer = gamePlayer;
    }

}