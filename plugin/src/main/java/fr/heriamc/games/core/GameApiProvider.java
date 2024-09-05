package fr.heriamc.games.core;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.api.GuiManager;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.core.event.GameEventBus;
import fr.heriamc.games.core.manager.GameGuiManager;
import fr.heriamc.games.core.pool.GamePoolRepository;
import fr.heriamc.games.engine.event.EventBus;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.proxy.packet.SendPlayerPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

@Getter
@Setter
public class GameApiProvider implements GameApi {

    private final EventBus eventBus;
    private final GuiManager guiManager;

    private final GamePoolManager gamePoolManager;

    private boolean devMode;

    private HeriaAPI heriaAPI;

    public GameApiProvider(JavaPlugin plugin) {
        this.eventBus = new GameEventBus(plugin);
        this.guiManager = new GameGuiManager(plugin);
        this.gamePoolManager = new GamePoolRepository();
        this.devMode = false;
        this.heriaAPI = HeriaAPI.get();
    }

    @Override
    public void redirectToHub(BaseGamePlayer gamePlayer) {
        final var hub = heriaAPI.getServerManager().getWithLessPlayers(HeriaServerType.HUB);

        heriaAPI.getMessaging().send(new SendPlayerPacket(gamePlayer.getUuid(), hub.getName()));
    }

    @Override
    public void sendDebug(Logger logger) {
        logger.info("""
                
                -----------[ Information ]-----------
                isDevMode: {}
                --------------------------------------""", devMode);
    }

    @Override
    public void sendAscii(Logger logger) {
        logger.info("""
                
                   ___                ___           _         \s
                  / __|__ _ _ __  ___| __|_ _  __ _(_)_ _  ___\s
                 | (_ / _` | '  \\/ -_) _|| ' \\/ _` | | ' \\/ -_)
                  \\___\\__,_|_|_|_\\___|___|_||_\\__, |_|_||_\\___|   enabled v1.0.0
                                              |___/           \s
                """);
    }

}