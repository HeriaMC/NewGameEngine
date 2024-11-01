package fr.heriamc.games.core;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.core.event.GameEventBus;
import fr.heriamc.games.core.pool.GamePoolRepository;
import fr.heriamc.games.engine.event.EventBus;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.proxy.queue.packet.QueueJoinPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

@Getter
@Setter
public class GameApiProvider implements GameApi {

    private final EventBus eventBus;
    private final GamePoolManager gamePoolManager;

    private boolean devMode;
    private boolean shutdown;

    private HeriaAPI heriaAPI;

    public GameApiProvider(JavaPlugin plugin) {
        this.eventBus = new GameEventBus(plugin);
        this.gamePoolManager = new GamePoolRepository();
        this.devMode = false;
        this.shutdown = false;
        this.heriaAPI = HeriaAPI.get();
    }

    @Override
    public void redirectToHub(Player player) {
        var heriaPlayer = heriaAPI.getPlayerManager().get(player.getUniqueId());

        if (heriaPlayer.isInQueue()) return;

        heriaAPI.getMessaging().send(new QueueJoinPacket(player.getUniqueId(), null, null, HeriaServerType.HUB, null));
    }

    @Override
    public void redirectToHub(BaseGamePlayer gamePlayer) {
        redirectToHub(gamePlayer.getPlayer());
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