package fr.heriamc.games.api.pool;

import fr.heriamc.api.game.packet.GameCreatedPacket;
import fr.heriamc.api.game.packet.GameCreationRequestPacket;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.core.GamePoolHeartBeat;
import fr.heriamc.games.engine.MiniGame;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public interface Pool {

    <M extends MiniGame> Class<M> getGameClass();

    String getName();
    HeriaServerType getType();

     int getMinPoolSize();
     int getMaxPoolSize();

     DirectConnectStrategy getStrategy();

    <M extends MiniGame> GameManager<M> getGamesManager();
    <M extends MiniGame> Map<UUID, M> getGameCreationCache();
    <M extends MiniGame> GamePoolHeartBeat<M> getGamePoolHeartBeat();

    void setup();
    void shutdown();

    void loadDefaultGames();

    void addGame();

    void addGame(GameCreationRequestPacket packet);
    void addGame(int number);

    void addGame(Object... objects);
    void addGame(GameCreationRequestPacket packet, Object... objects);
    void addGame(int number, Object... objects);

    void addGame(UUID uuid, Object object);
    void addGame(GameCreationRequestPacket packet, UUID uuid, Object object);
    void addGame(int number, UUID uuid, Object object);

    void addNecessaryGame();

    void sendGameCreatedPacket(GameCreatedPacket packet);

    void sendDebugMessage();
    String getDebugMessage();

    default void useCustomStrategy(Player player) {
        throw new NoSuchMethodError();
    }

}