package fr.heriamc.games.core.command;

import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.core.GameEngine;
import fr.heriamc.games.engine.utils.CacheUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Slf4j
@AllArgsConstructor
public class DebugCommand {

    private final GameEngine plugin;

    /*
        DEBUG GAME
     */

    @HeriaCommand(name = "showListeners")
    public void showListenersCommand(CommandArgs commandArgs) {
        commandArgs.getSender().sendMessage("LISTENERS: " + GameApi.getInstance().getEventBus().getListeners());
        commandArgs.getSender().sendMessage("REGISTERED LISTENERS: " + GameApi.getInstance().getEventBus().getRegisteredListeners());
    }

    @HeriaCommand(name = "showThread")
    public void showThreadCommand(CommandArgs commandArgs) {
        commandArgs.getSender().sendMessage("THREADS: " + Thread.activeCount());
    }

    @HeriaCommand(name = "gameDebug")
    public void gameDebugCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        plugin.getGamePool().getGamesManager().getGame(player, game -> game.sendDebugInfoMessage(player));
    }

    @HeriaCommand(name = "cachedebug")
    public void sendCacheDebug(CommandArgs commandArgs) {
        CacheUtils.sendDebug();
        CacheUtils.dynamicCacheMap.forEach((name, cache) -> commandArgs.getSender().sendMessage(name + ": " + cache.getDebugMessage()));
    }

}