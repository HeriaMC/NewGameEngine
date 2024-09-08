package fr.heriamc.games.engine.event.game;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GameEvent;

public class GameRemovedEvent<M extends MiniGame> extends GameEvent<M> {

    public GameRemovedEvent(M game) {
        super(game);
    }

}