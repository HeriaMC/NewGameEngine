package fr.heriamc.games.core.pool;

import fr.heriamc.api.game.packet.GameJoinPacket;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.api.pool.Pool;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.CacheUtils;
import fr.heriamc.games.engine.utils.cache.DynamicCache;
import fr.heriamc.games.engine.utils.cache.TimedCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class GamePoolRepository implements GamePoolManager {

    private final List<Pool> gamePools;

    private final DynamicCache<UUID, GameJoinPacket> joinPacketCache;

    public GamePoolRepository() {
        this.gamePools = new ArrayList<>();
        this.joinPacketCache = CacheUtils.getCache("join-cache", new TimedCache<>(10, TimeUnit.SECONDS));
    }

    @Override
    public void addPool(Pool gamePool) {
        gamePools.add(gamePool);
    }

    @Override
    public void removePool(Pool gamePool) {
        gamePools.removeIf(gamePool::equals);
    }

    @Override
    public void shutdown() {
        gamePools.forEach(Pool::shutdown);
    }

    @Override
    public void joinWithPacket(PlayerLoginEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();
        var packet = joinPacketCache.getIfPresent(uuid);

        if (packet == null) {
            player.sendMessage("[GamePoolManager] YOU JOINED ABNORMALLY THIS SERVER YOU SHOULD GO BACK TO HUB");
            log.info("[GamePoolManager] {} join without packet", player.getName());
            return;
        }

        if (packet.getGameName().contains("-")) {
            log.info("[GamePoolManager] {} try to join game '{}'", player.getName(), packet.getGameName());

            getGameByID(packet.getGameName())
                    .filter(game -> game.canJoin() || packet.isSpectator())
                    .ifPresentOrElse(
                            game -> event.allow(),
                            () -> denyConnection(event, uuid, "La partie '" + packet.getGameName() + "' n'est plus disponible")
                    );

            return;
        }

        log.info("[GamePoolManager] {} try to join pool '{}'", player.getName(), packet.getGameName());

        getGamePool(packet.getGameName()).ifPresentOrElse(
                gamePool -> event.allow(),
                () -> denyConnection(event, uuid, "Aucune partie disponible"));
    }

    private void denyConnection(PlayerLoginEvent event, UUID uuid, String message) {
        log.info("[GamePoolManager] Connection denied for {} with message '{}'", event.getPlayer().getName(), message);
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
        joinPacketCache.invalidate(uuid);
    }

    @Override
    public void leaveGame(Player player) {
        gamePools.forEach(gamePool -> gamePool.getGamesManager().leaveGame(player));
    }

    @Override
    public Optional<Pool> getGamePool(String name) {
        return gamePools.stream()
                .filter(gamePool -> gamePool.getName().contains(name)).findFirst();
    }

    @Override
    public Optional<Pool> getGamePool(HeriaServerType type) {
        return  gamePools.stream()
                .filter(gamePool -> gamePool.getType() == type).findFirst();
    }

    @Override
    public Optional<MiniGame> getGame(Player player) {
        for (Pool gamePool : gamePools)
            return gamePool.getGamesManager().getGame(player);
        return Optional.empty();
    }

    @Override
    public Optional<MiniGame> getGameByID(String id) {
        for (Pool gamePool : gamePools)
            return gamePool.getGamesManager().getGameByID(id);
        return Optional.empty();
    }

    @Override
    public MiniGame getNullableGame(Player player) {
        for (Pool gamePool : gamePools)
            return gamePool.getGamesManager().getGame(player).orElse(null);
        return null;
    }

    @Override
    public MiniGame getNullableGameByID(String id) {
        for (Pool gamePool : gamePools)
            return gamePool.getGamesManager().getGameByID(id).orElse(null);
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends MiniGame> GamePool<M> getGamePool(Class<M> clazz) {
        return gamePools.stream()
                .filter(gamePool -> clazz.isAssignableFrom(gamePool.getGameClass()))
                .map(gamePool -> (GamePool<M>) gamePool)
                .findFirst().orElse(null);
    }

    @Override
    public Pool getGamePoolByClass(Class<Pool> clazz) {
        return gamePools.stream()
                .filter(gamePool -> gamePool.getClass().isInstance(clazz))
                .map(clazz::cast)
                .findFirst().orElse(null);
    }

    @Override
    public List<MiniGame> getAllGames() {
        return gamePools.stream()
                .flatMap(pool -> pool.getGamesManager().getGames().stream())
                .toList();
    }

    @Override
    public List<Pool> getGamePoolByStrategy(DirectConnectStrategy strategy) {
        return gamePools.stream()
                .filter(gamePool -> gamePool.getStrategy().equals(strategy))
                .toList();
    }

    @Override
    public List<Pool> getGamePoolByStrategy(EnumSet<DirectConnectStrategy> strategies) {
        return gamePools.stream()
                .filter(gamePool -> strategies.contains(gamePool.getStrategy()))
                .toList();
    }

    @Override
    public List<Pool> getGamePoolWithoutStrategy(DirectConnectStrategy strategy) {
        return gamePools.stream()
                .filter(gamePool -> !gamePool.getStrategy().equals(strategy))
                .toList();
    }

    @Override
    public List<Pool> getGamePoolWithoutStrategy(EnumSet<DirectConnectStrategy> strategies) {
        return gamePools.stream()
                .filter(gamePool -> !strategies.contains(gamePool.getStrategy()))
                .toList();
    }

    @Override
    public int getGamesCount() {
        return gamePools.stream()
                .mapToInt(gamePool -> gamePool.getGamesManager().getSize()).sum();
    }

    @Override
    public String getDebugMessage() {
        return gamePools.stream()
                .map(Pool::getDebugMessage)
                .collect(Collectors.joining(" || "));
    }

}