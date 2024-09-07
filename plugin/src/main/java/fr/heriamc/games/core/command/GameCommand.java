package fr.heriamc.games.core.command;

import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.games.core.GameEngine;
import org.bukkit.entity.Player;

public record GameCommand(GameEngine plugin) {

    @HeriaCommand(name = "game", usage = "/game <info>")
    public void gameDebugCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        if (commandArgs.getArgs().length == 0) {
            player.sendMessage("/game <info>");
            return;
        }

        switch (commandArgs.getArgs(0)) {
            case "info" -> executeInfoCommand(player, commandArgs);
            default -> player.sendMessage("/game <info>");
        }
    }

    private void executeInfoCommand(Player player, CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();

        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            plugin.getGameApi().getGamePoolManager().getGamePools().stream()
                    .flatMap(gamePool -> gamePool.getGamesManager().getGames().stream())
                    .forEach(game -> game.sendDebugInfoMessage(player));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            plugin.getGameApi().getGamePoolManager().getGameByID(args[1]).ifPresentOrElse(
                    game -> game.sendDebugInfoMessage(player),
                    () -> player.sendMessage("Game introuvable"));
            return;
        }

        player.sendMessage("/game [info] (id)");
    }

}