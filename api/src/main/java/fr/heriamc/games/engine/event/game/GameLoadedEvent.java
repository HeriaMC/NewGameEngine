package fr.heriamc.games.engine.event.game;

import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.event.GameEvent;
import lombok.Getter;

@Getter
public class GameLoadedEvent<M extends MiniGame> extends GameEvent<M> {

    private final GamePool<M> gamePool;

    public GameLoadedEvent(M game, GamePool<M> gamePool) {
        super(game);
        this.gamePool = gamePool;
    }

}