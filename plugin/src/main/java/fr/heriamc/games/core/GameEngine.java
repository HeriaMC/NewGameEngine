package fr.heriamc.games.core;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.messaging.packet.HeriaPacketChannel;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.HeriaCommandManager;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.core.command.DebugCommand;
import fr.heriamc.games.core.command.GameCommand;
import fr.heriamc.games.core.command.ThreadGuiCommand;
import fr.heriamc.games.core.listener.GameCancelListener;
import fr.heriamc.games.core.listener.GameConnectionListener;
import fr.heriamc.games.core.listener.GamePacketListener;
import fr.heriamc.games.core.test.ExampleGamePool;
import fr.heriamc.games.engine.utils.CacheUtils;
import fr.heriamc.games.engine.utils.GameSizeTemplate;
import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.author.Authors;

import java.util.UUID;

@Slf4j
@Getter
@Authors(@Author("Joupi"))
@Plugin(name = "HeriaGameEngine", version = "1.0.0")
@DependsOn({ @Dependency("SlimeWorldManager"), @Dependency("HeriaAPI") })
public class GameEngine extends JavaPlugin {

    private GameApi gameApi;

    private ExampleGamePool gamePool;

    @Override
    public void onEnable() {
        this.gameApi = GameApi.setProvider(new GameApiProvider(this));
        BukkitThreading.setPlugin(this);

        gameApi.sendAscii(log);
        gameApi.setDevMode(false);

        var heriaApi = HeriaAPI.get();
        var commandManager = new HeriaCommandManager(this);

        if (gameApi.isDevMode()) {
            this.gamePool = new ExampleGamePool();

            commandManager.registerCommand(new DebugCommand(this));
            gameApi.getGamePoolManager().addPool(gamePool);

            gamePool.addGame(2);
            gamePool.addGame(UUID.randomUUID(), GameSizeTemplate.SIZE_4V4.toGameSize());
        }

        commandManager.registerCommand(new ThreadGuiCommand());
        commandManager.registerCommand(new GameCommand(gameApi.getGamePoolManager()));

        gameApi.getEventBus().registerListeners(
                new GameConnectionListener(gameApi.getGamePoolManager()),
                new GameCancelListener()
        );

        heriaApi.getMessaging()
                .registerReceiver(HeriaPacketChannel.GAME, new GamePacketListener(gameApi.getGamePoolManager(), heriaApi));
    }

    @Override
    public void onDisable() {
        gameApi.setShutdown(true);
        gameApi.getGamePoolManager().shutdown();
        CacheUtils.cleanRemoveAll();

        /*VirtualThreading.shutdown();
        MultiThreading.shutdown();*/

        log.info("GAME ENGINE BYE BYE.");
    }

}