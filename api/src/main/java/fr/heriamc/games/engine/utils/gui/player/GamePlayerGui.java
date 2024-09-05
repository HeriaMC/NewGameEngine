package fr.heriamc.games.engine.utils.gui.player;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.gui.game.GameGui;
import lombok.Getter;

@Getter
public abstract class GamePlayerGui<M extends MiniGame, G extends BaseGamePlayer> extends GameGui<M> {

    private final G gamePlayer;

    public GamePlayerGui(M game, G gamePlayer, String inventoryName, int rows) {
        super(game, inventoryName, rows);
        this.gamePlayer = gamePlayer;
    }

}