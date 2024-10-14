package fr.heriamc.games.engine.event.player;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GamePlayerEvent;

public class GamePlayerJoinEvent<M extends MiniGame, G extends BaseGamePlayer> extends GamePlayerEvent<M, G> {

    public GamePlayerJoinEvent(M game, G gamePlayer) {
        super(game, gamePlayer);
    }

    public void sendJoinMessage(String message) {
        game.broadcast(message);
    }

    public void sendJoinMessage(String... messages) {
        game.broadcast(messages);
    }

}