package fr.heriamc.games.api.addon;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.api.pool.Pool;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Addon {

    Logger log = LoggerFactory.getLogger(Addon.class);

    void enable();
    void disable();

    Pool getPool();

    GameApi getApi();

    HeriaAPI getHeriaApi();
    HeriaBukkit getHeriaBukkit();

    void registerListener(Listener listener);
    void registerListener(Listener... listeners);

    void registerCommand(Object object);
    void registerCommand(Object... objects);

    void openGui(HeriaMenu menu);

    void redirectToHub(Player player);
    void redirectToHub(BaseGamePlayer gamePlayer);

    void sendDebug();

}