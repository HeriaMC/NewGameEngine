package fr.heriamc.games.api;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.gui.Gui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public interface GuiManager extends Listener {

    ConcurrentMap<UUID, Gui> getGuis();

    Optional<Gui> getGui(UUID uuid);

    Optional<Gui> getGui(HumanEntity player);
    Optional<Gui> getGui(BaseGamePlayer gamePlayer);

    void open(HumanEntity humanEntity, Gui gui);
    void open(Player player, Gui gui);
    void open(BaseGamePlayer gamePlayer, Gui gui);

}