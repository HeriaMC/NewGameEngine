package fr.heriamc.games.engine.utils.gui.game;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.gui.Gui;
import lombok.Getter;

@Getter
public abstract class GameGui<M extends MiniGame> extends Gui {

    private final M game;

    public GameGui(M game, String inventoryName, int rows) {
        super(inventoryName, rows);
        this.game = game;
    }

}