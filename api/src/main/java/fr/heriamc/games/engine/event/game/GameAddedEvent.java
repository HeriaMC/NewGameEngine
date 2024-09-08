package fr.heriamc.games.engine.event.game;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GameEvent;

public class GameAddedEvent<M extends MiniGame> extends GameEvent<M> {

    public GameAddedEvent(M game) {
        super(game);
    }

}