package fr.heriamc.games.core.command;

import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.games.core.GameEngine;
import fr.heriamc.games.engine.utils.CacheUtils;
import org.bukkit.entity.Player;

public record DebugCommand(GameEngine plugin) {

    /*
        DEBUG GAME
        UNUSED SHOULD BE DELETED OR REMADE FOR ADD SOME AND OTHERS DEBUG NECESSARY FOR FOUND PROBLEMS
     */

    @HeriaCommand(name = "showListeners")
    public void showListenersCommand(CommandArgs commandArgs) {
        commandArgs.getSender().sendMessage("LISTENERS: " + plugin.getGameApi().getEventBus().getListeners());
        commandArgs.getSender().sendMessage("REGISTERED LISTENERS: " + plugin.getGameApi().getEventBus().getRegisteredListeners());
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