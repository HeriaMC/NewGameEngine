package fr.heriamc.games.api.addon;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.HeriaCommandManager;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.api.pool.Pool;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public abstract class GameAddon<P extends Pool> extends JavaPlugin implements Addon {

    protected final P pool;

    protected GameApi api;

    protected HeriaAPI heriaApi;
    protected HeriaBukkit heriaBukkit;
    protected HeriaCommandManager commandManager;

    @Override
    public void onEnable() {
        this.api = GameApi.getInstance();
        this.heriaApi = HeriaAPI.get();
        this.heriaBukkit = HeriaBukkit.get();
        this.commandManager = heriaBukkit.getCommandManager();

        api.getGamePoolManager().addPool(pool);
        pool.setup();

        enable();
        pool.sendDebugMessage();
    }

    @Override
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void registerListener(Listener... listeners) {
        Arrays.asList(listeners)
                .forEach(this::registerListener);
    }

    @Override
    public void registerCommand(Object object) {
        commandManager.registerCommand(object);
    }

    @Override
    public void registerCommand(Object... objects) {
        Arrays.asList(objects)
                .forEach(this::registerCommand);
    }

    @Override
    public void openGui(HeriaMenu menu) {
        heriaBukkit.getMenuManager().open(menu);
    }

    @Override
    public void redirectToHub(Player player) {
        api.redirectToHub(player);
    }

    @Override
    public void redirectToHub(BaseGamePlayer gamePlayer) {
        api.redirectToHub(gamePlayer);
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    @Override
    public void sendDebug() {

    }

}