package fr.heriamc.games.engine.event.player;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GamePlayerEvent;
import fr.heriamc.games.engine.player.BaseGamePlayer;

public class GamePlayerSpectateEvent<M extends MiniGame, G extends BaseGamePlayer> extends GamePlayerEvent<M, G> {

    public GamePlayerSpectateEvent(M game, G gamePlayer) {
        super(game, gamePlayer);
    }

}