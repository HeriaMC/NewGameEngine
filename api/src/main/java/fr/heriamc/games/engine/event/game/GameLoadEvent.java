package fr.heriamc.games.engine.event.game;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GameEvent;

public class GameLoadEvent<M extends MiniGame> extends GameEvent<M> {

    public GameLoadEvent(M game) {
        super(game);
    }

}