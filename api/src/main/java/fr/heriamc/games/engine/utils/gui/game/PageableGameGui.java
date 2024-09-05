package fr.heriamc.games.engine.utils.gui.game;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.gui.PageableGui;
import lombok.Getter;

@Getter
public abstract class PageableGameGui<M extends MiniGame, E> extends PageableGui<E> {

    private final M game;

    public PageableGameGui(M game, String inventoryName, boolean updatable, int rows, int maxItems) {
        super(inventoryName, updatable, rows, maxItems);
        this.game = game;
    }

}