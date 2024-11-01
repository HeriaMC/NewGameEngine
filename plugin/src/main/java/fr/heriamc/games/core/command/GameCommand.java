package fr.heriamc.games.core.command;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.bukkit.menu.HeriaMenuManager;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.core.gui.game.GameGui;
import org.bukkit.entity.Player;

public record GameCommand(HeriaMenuManager menuManager, GamePoolManager gamePoolManager) {

    @HeriaCommand(name = "game", power = HeriaRank.PLAYER, showInHelp = false)
    public void gameDebugCommand(CommandArgs commandArgs) {
        var player = commandArgs.getPlayer();

        if (commandArgs.getArgs().length == 0) {
            player.sendMessage("/game <info/menu> (id)");
            return;
        }

        switch (commandArgs.getArgs(0)) {
            case "info" -> executeInfoCommand(player, commandArgs);
            case "menu" -> menuManager.open(new GameGui(player, gamePoolManager));
            default -> player.sendMessage("/game <info/menu> (id)");
        }
    }

    private void executeInfoCommand(Player player, CommandArgs commandArgs) {
        var args = commandArgs.getArgs();

        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            gamePoolManager.getAllGames()
                    .forEach(game -> game.sendDebugInfoMessage(player));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            gamePoolManager.getGameByID(args[1]).ifPresentOrElse(
                    game -> game.sendDebugInfoMessage(player),
                    () -> player.sendMessage("Game introuvable"));
            return;
        }

        player.sendMessage("/game info (id)");
    }

}