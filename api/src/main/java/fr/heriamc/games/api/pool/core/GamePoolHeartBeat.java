package fr.heriamc.games.api.pool.core;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.game.HeriaGameInfo;
import fr.heriamc.bukkit.game.HeriaGameManager;
import fr.heriamc.bukkit.game.HeriaGamesList;
import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GamePoolHeartBeat<M extends MiniGame> implements Runnable {

    private final List<M> games;

    private final HeriaGameManager gameManager;
    private final HeriaGamesList gamesList;

    public GamePoolHeartBeat(GamePool<M> gamePool) {
        this.games = gamePool.getGamesManager().getGames();
        this.gameManager = HeriaBukkit.get().getHeriaGameManager();
        this.gamesList = new HeriaGamesList(gamePool.getType(), new ArrayList<>(gamePool.getMaxPoolSize()));
        VirtualThreading.scheduledPool.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        gameManager.put(gamesList.setGames(getGameInfos()));
    }

    public void shutdown() {
        gameManager.getAllInCache().forEach(gameManager::remove);
    }

    public List<HeriaGameInfo> getGameInfos() {
        return games.stream().map(this::toGameInfo).toList();
    }

    public HeriaGameInfo toGameInfo(M game) {
        return new HeriaGameInfo(
                game.getFullName(),
                new ArrayList<>(game.getPlayers().keySet()),
                game.getSize(), game.getAlivePlayersCount(),
                game.getSpectatorsCount(),
                game.getState()
        );
    }

}
