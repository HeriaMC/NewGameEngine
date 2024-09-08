package fr.heriamc.games.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public record GameCancelListener() implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    /*
        FIX INVISIBLE PLAYER ON SPAWN (IDK IF IT WORKING)
     */
    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        var player = event.getPlayer();

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) return;

            onlinePlayer.hidePlayer(player);
            onlinePlayer.showPlayer(player);
        }
    }
}