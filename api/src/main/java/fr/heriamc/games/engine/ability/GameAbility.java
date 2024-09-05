package fr.heriamc.games.engine.ability;

import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.MiniGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

@Getter
@Setter
public abstract class GameAbility<M extends MiniGame, E extends PlayerEvent> implements Listener {

    private final String name;

    private final Class<M> gameClass;
    private final Class<E> eventClazz;

    private boolean enabled;

    public GameAbility(String name, Class<M> gameClass, Class<E> eventClass) {
        this.name = name;
        this.gameClass = gameClass;
        this.eventClazz = eventClass;
        GameApi.getInstance().getEventBus().registerListener(gameClass, this);
    }

    public abstract void onUse(M game, UUID user, E event);

}