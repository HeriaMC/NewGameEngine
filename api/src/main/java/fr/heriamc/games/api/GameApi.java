package fr.heriamc.games.api;

import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.engine.event.EventBus;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

public interface GameApi {

    EventBus getEventBus();
    GamePoolManager getGamePoolManager();

    boolean isDevMode();
    boolean isShutdown();

    void redirectToHub(Player player);
    void redirectToHub(BaseGamePlayer gamePlayer);

    void setDevMode(boolean devMode);
    void setShutdown(boolean shutdown);

    void sendDebug(Logger logger);
    void sendAscii(Logger logger);

    static GameApi getInstance() {
        return Provider.getProvider();
    }

    static GameApi setProvider(GameApi api) {
        return Provider.setProvider(api);
    }

    class Provider {

        @Getter
        private static GameApi provider;

        public static GameApi setProvider(GameApi provider) {
            return Provider.provider = provider;
        }

    }

}