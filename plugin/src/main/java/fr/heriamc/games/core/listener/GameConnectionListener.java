package fr.heriamc.games.core.listener;

import fr.heriamc.api.game.packet.GameJoinPacket;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.engine.event.player.GamePlayerSpectateEvent;
import fr.heriamc.games.engine.utils.CacheUtils;
import fr.heriamc.games.engine.utils.cache.DynamicCache;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@Slf4j
public record GameConnectionListener(GamePoolManager gamePoolManager, DynamicCache<UUID, GameJoinPacket> cache) implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        gamePoolManager.joinWithPacket(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var packet = cache.getIfPresent(player.getUniqueId());

        if (packet == null) return;

        if (packet.getGameName().contains("-"))
            gamePoolManager
                    .getGameByID(packet.getGameName())
                    .filter(game -> game.canJoin() || packet.isSpectator())
                    .ifPresent(game -> {
                        game.joinGame(player, packet.isSpectator());
                        cache.invalidate(player.getUniqueId());
                    });
        else
            gamePoolManager
                    .getGamePool(packet.getGameName())
                    .ifPresent(gamePool -> {
                        gamePool.getGamesManager().findGame(player);
                        cache.invalidate(player.getUniqueId());
                    });
    }

    // MAKE THIS ?!
    @EventHandler
    public void onPlayerSpectate(GamePlayerSpectateEvent<?, ?> event) {
        var player = event.getPlayer();
        var gamePlayer = event.getGamePlayer();

        gamePlayer.sendMessage("You joined as spectator");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();

        CacheUtils.cooldowns
                .values()
                .forEach(cooldown -> cooldown.invalidate(player.getUniqueId()));

        gamePoolManager.leaveGame(player);
        cache.invalidate(player.getUniqueId());
    }

}