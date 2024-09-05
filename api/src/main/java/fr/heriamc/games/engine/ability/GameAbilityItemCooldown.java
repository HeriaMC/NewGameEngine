package fr.heriamc.games.engine.ability;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.CacheUtils;
import fr.heriamc.games.engine.utils.cache.Cooldown;
import fr.heriamc.games.engine.utils.cache.CooldownCache;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class GameAbilityItemCooldown<M extends MiniGame, E extends PlayerEvent> extends GameAbilityItem<M, E> {

    private final int duration;

    private final Cooldown cooldown;
    private ScheduledFuture<?> future;

    public GameAbilityItemCooldown(String name, Class<M> gameClass, Class<E> eventClass, int duration, TimeUnit timeUnit, ItemStack itemStack) {
        super(name, gameClass, eventClass, itemStack);
        this.duration = duration;
        this.cooldown = CacheUtils.getCooldown(name,
                new CooldownCache(duration, timeUnit, (key, value, cause) -> future.cancel(false)));
    }

    public void handleAbility(M game, UUID user, E event) {
        Player player = event.getPlayer();

        if (cooldown.contains(user)) return;
        if (future != null && !future.isDone()) future.cancel(false);

        cooldown.put(user);

        future = MultiThreading.schedule(() -> {
            if (!cooldown.contains(user)) {
                future.cancel(false);
                return;
            }

            //TitleAPI.sendActionBar(player, "Cooldown: " + cooldown.getTimeLeftInSeconds(user) + " seconds");
        }, 0, 1, TimeUnit.SECONDS);

        onUse(game, user, event);
        CacheUtils.sendDebug();
    }

}