package fr.heriamc.games.core.command;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.games.core.gui.ThreadGui;

public record ThreadGuiCommand(HeriaBukkit heriaBukkit) {

    @HeriaCommand(name = "threads", inGameOnly = true, power = HeriaRank.PLAYER, showInHelp = false)
    public void onExecute(CommandArgs commandArgs) {
        heriaBukkit.getMenuManager()
                .open(new ThreadGui(commandArgs.getPlayer()));
    }

}