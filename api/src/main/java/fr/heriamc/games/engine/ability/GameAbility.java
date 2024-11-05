package fr.heriamc.games.engine.ability;

import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

@Getter
@Setter
public abstract class GameAbility<M extends MiniGame, G extends BaseGamePlayer, E extends PlayerEvent> implements Listener {

    protected final String name;

    private final Class<M> gameClass;
    private final Class<E> eventClazz;

    protected boolean enabled;

    public GameAbility(String name, Class<M> gameClass, Class<E> eventClass) {
        this.name = name;
        this.gameClass = gameClass;
        this.eventClazz = eventClass;
        GameApi.getInstance().getEventBus().registerListener(gameClass, this);
    }

    public abstract void onUse(M game, G user, E event);

}