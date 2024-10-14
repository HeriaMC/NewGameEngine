package fr.heriamc.games.api.processor;

import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.engine.MiniGame;

public interface GamePoolProcessor<M extends MiniGame> {

    GamePool<M> getGamePool();

    void process(M game);

}