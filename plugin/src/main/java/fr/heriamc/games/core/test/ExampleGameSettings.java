package fr.heriamc.games.core.test;

import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.games.engine.GameSettings;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.board.GameBoard;
import fr.heriamc.games.engine.player.BaseGamePlayer;

public class ExampleGameSettings extends GameSettings<ExampleGameManager> {

    public ExampleGameSettings(GameSize gameSize) {
        super(gameSize);
    }

    /*@Override
    public <G extends MiniGame, P extends BaseGamePlayer> GameBoard<?, ?> defaultBoard(G game, P gamePlayer) {
        return new ExampleBoard((ExampleGame) game, (ExampleGamePlayer) gamePlayer);
    }*/

    @Override
    public GameBoard<?, ?> defaultBoard(MiniGame game, BaseGamePlayer gamePlayer) {
        return new ExampleBoard((ExampleGame) game, (ExampleGamePlayer) gamePlayer);
    }

}