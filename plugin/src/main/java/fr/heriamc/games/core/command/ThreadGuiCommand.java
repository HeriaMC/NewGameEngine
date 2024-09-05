package fr.heriamc.games.core.command;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.games.core.gui.NewThreadGui;

public class ThreadGuiCommand {

    @HeriaCommand(name = "threads", inGameOnly = true)
    public void onExecute(CommandArgs commandArgs) {
        HeriaBukkit.get().getMenuManager().open(new NewThreadGui(commandArgs.getPlayer()));
    }

}