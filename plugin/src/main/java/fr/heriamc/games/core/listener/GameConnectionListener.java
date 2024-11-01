package fr.heriamc.games.core.listener;

import fr.heriamc.api.game.packet.GameJoinPacket;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.engine.event.player.GamePlayerSpectateEvent;
import fr.heriamc.games.engine.utils.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Slf4j
public record GameConnectionListener(GamePoolManager gamePoolManager) implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        var uuid = event.getPlayer().getUniqueId();

        /*
            DEBUG SHOULD BE CHANGED !!!
         */
        var name = gamePoolManager.getGamePools().getFirst().getName();
        var packet = new GameJoinPacket(uuid, name,false);

        gamePoolManager.getJoinPacketCache().put(uuid, packet);
        gamePoolManager.joinWithPacket(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var packet = gamePoolManager.getJoinPacketCache().getIfPresent(player.getUniqueId());

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
        gamePoolManager.getJoinPacketCache().invalidate(player.getUniqueId());
    }

}