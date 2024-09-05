package fr.heriamc.games.engine.board;

import fr.heriamc.bukkit.scoreboard.PersonalScoreboard;
import fr.heriamc.games.engine.SimpleGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;

public abstract class GameBoard<M extends SimpleGame<G, ?>, G extends BaseGamePlayer> extends PersonalScoreboard {

    protected final M game;
    protected final G gamePlayer;

    public GameBoard(M game, G gamePlayer) {
        super(gamePlayer.getPlayer());
        this.game = game;
        this.gamePlayer = gamePlayer;
    }

}