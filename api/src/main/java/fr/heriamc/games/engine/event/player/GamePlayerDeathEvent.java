package fr.heriamc.games.engine.event.player;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GamePlayerEvent;

public class GamePlayerDeathEvent<M extends MiniGame, G extends BaseGamePlayer> extends GamePlayerEvent<M, G> {

    public GamePlayerDeathEvent(M game, G gamePlayer) {
        super(game, gamePlayer);
    }

}