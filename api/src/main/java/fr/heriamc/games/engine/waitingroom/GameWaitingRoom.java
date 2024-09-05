package fr.heriamc.games.engine.waitingroom;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.map.Map;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.task.CountdownTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class GameWaitingRoom<M extends MiniGame, G extends BaseGamePlayer> implements WaitingRoom {

    protected final M game;

    protected Map map;
    protected CountdownTask countdownTask;

    public abstract void giveItems(G gamePlayer);

    /*
        giveItems();
        tryToStartTimer();
     */

    /*
          tryToCancelTimer();
     */

    public void tryToStartTimer() {
        if (game.canStart())
            countdownTask.run();
    }

    public void tryToCancelTimer() {
        if (!game.canStart() && countdownTask.isStarted())
            countdownTask.cancel();
    }

}