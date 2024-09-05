package fr.heriamc.games.api.pool;

import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.core.GamePoolHeartBeat;
import fr.heriamc.games.engine.MiniGame;
import org.bukkit.entity.Player;

public interface Pool {

    <M extends MiniGame> Class<M> getGameClass();

    String getName();
    HeriaServerType getType();

     int getMinPoolSize();
     int getMaxPoolSize();

     DirectConnectStrategy getStrategy();

    <M extends MiniGame> GameManager<M> getGamesManager();
    <M extends MiniGame> GamePoolHeartBeat<M> getGamePoolHeartBeat();

    void setup();
    void shutdown();

    void sendDebugMessage();
    String getDebugMessage();

    default void useCustomStrategy(Player player) {
        throw new NoSuchMethodError();
    }

}