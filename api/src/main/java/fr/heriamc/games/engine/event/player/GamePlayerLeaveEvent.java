package fr.heriamc.games.engine.event.player;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GamePlayerEvent;
import fr.heriamc.games.engine.player.BaseGamePlayer;

public class GamePlayerLeaveEvent<M extends MiniGame, G extends BaseGamePlayer> extends GamePlayerEvent<M, G> {

    public GamePlayerLeaveEvent(M game, G gamePlayer) {
        super(game, gamePlayer);
    }

    public void sendLeaveMessage(String message) {
        game.broadcast(message);
    }

    public void sendLeaveMessage(String... messages) {
        game.broadcast(messages);
    }

}