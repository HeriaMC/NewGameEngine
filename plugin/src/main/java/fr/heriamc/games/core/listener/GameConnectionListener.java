package fr.heriamc.games.core.listener;

import fr.heriamc.bukkit.game.packet.GameJoinPacket;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.core.GameEngine;
import fr.heriamc.games.engine.utils.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Slf4j
public record GameConnectionListener(GameEngine plugin, GamePoolManager gamePoolManager) implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        String name = gamePoolManager.getGamePools().getFirst().getName();

        GameJoinPacket packet = new GameJoinPacket(player.getUniqueId(), name,false);

        gamePoolManager.getJoinPacketCache().put(player.getUniqueId(), packet);
        gamePoolManager.joinWithPacket(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GameJoinPacket packet = gamePoolManager.getJoinPacketCache().getIfPresent(player.getUniqueId());

        if (packet == null) return;

        if (packet.getGameName().contains("-"))
            gamePoolManager
                    .getGameByID(packet.getGameName())
                    .filter(game -> game.canJoin() || packet.isSpectator())
                    .ifPresent(game -> game.joinGame(player, packet.isSpectator()));
        else
            gamePoolManager
                    .getGamePool(packet.getGameName())
                    .ifPresent(gamePool -> gamePool.getGamesManager().findGame(player));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        CacheUtils.cooldowns
                .values()
                .forEach(cooldown -> cooldown.invalidate(player.getUniqueId()));

        gamePoolManager.leaveGame(player);
        gamePoolManager.getJoinPacketCache().invalidate(player.getUniqueId());
    }

}