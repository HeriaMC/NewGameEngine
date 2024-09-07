package fr.heriamc.games.engine.waitingroom;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.map.Map;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.task.CountdownTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.GameMode;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class GameWaitingRoom<M extends MiniGame, G extends BaseGamePlayer> {

    protected final M game;

    protected Map map;
    protected CountdownTask countdownTask;

    public abstract void onJoin(G gamePlayer);
    public abstract void onLeave(G gamePlayer);

    protected abstract void giveItems(G gamePlayer);

    public void processJoin(G gamePlayer) {
        cleanUpPlayer(gamePlayer);

        map.getSpawn().syncTeleport(gamePlayer);
        onJoin(gamePlayer);
        giveItems(gamePlayer);

        tryToStartTimer();
    }

    public void processLeave(G gamePlayer) {
        onLeave(gamePlayer);
        tryToCancelTimer();
    }

    public void cleanUpPlayer(G gamePlayer) {
        var player = gamePlayer.getPlayer();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().clear();

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0f);
    }

    public void tryToStartTimer() {
        if (game.canStart())
            countdownTask.run();
    }

    public void tryToCancelTimer() {
        if (!game.canStart() && countdownTask.isStarted())
            countdownTask.cancel();
    }

}