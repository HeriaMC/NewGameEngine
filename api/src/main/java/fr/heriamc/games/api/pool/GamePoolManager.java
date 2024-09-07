package fr.heriamc.games.api.pool;

import fr.heriamc.bukkit.game.packet.GameJoinPacket;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.cache.DynamicCache;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GamePoolManager {

    List<Pool> getGamePools();
    DynamicCache<UUID, GameJoinPacket> getJoinPacketCache();

    void addPool(Pool gamePool);
    void removePool(Pool gamePool);

    void shutdown();

    void joinWithPacket(PlayerLoginEvent event);

    void leaveGame(Player player);

    Optional<Pool> getGamePool(String name);

    Optional<MiniGame> getGameByID(String id);

    <M extends MiniGame> Pool getGamePool(Class<M> clazz);

    Pool getGamePoolByClass(Class<Pool> clazz);

    List<Pool> getGamePoolByStrategy(DirectConnectStrategy strategy);
    List<Pool> getGamePoolByStrategy(EnumSet<DirectConnectStrategy> strategies);

    List<Pool> getGamePoolWithoutStrategy(DirectConnectStrategy strategy);
    List<Pool> getGamePoolWithoutStrategy(EnumSet<DirectConnectStrategy> strategies);

    int getGamesCount();

    String getDebugMessage();

}