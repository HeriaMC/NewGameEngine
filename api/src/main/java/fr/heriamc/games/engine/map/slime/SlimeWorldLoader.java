package fr.heriamc.games.engine.map.slime;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import fr.heriamc.games.engine.map.GameMapLoader;
import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import fr.heriamc.games.engine.utils.func.ThrowingRunnable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@AllArgsConstructor
public class SlimeWorldLoader implements GameMapLoader<SlimeMap> {

    private static final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    private final SlimeLoader slimeLoader;

    public SlimeWorldLoader(String slimeLoaderName) {
        this.slimeLoader = slimePlugin.getLoader(slimeLoaderName);
    }

    public SlimeWorldLoader() {
        this("file");
    }

    public File getWorldDir(String dirName) {
        return new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + dirName);
    }

    public CompletableFuture<SlimeMap> load(SlimeMap map) {
        CompletableFuture<SlimeMap> future = new CompletableFuture<>();

        BukkitThreading.runTaskAsynchronously(() -> {
            try {
                var start = System.currentTimeMillis();
                var properties = new SlimePropertyMap();

                properties.setString(SlimeProperties.DIFFICULTY, "normal");
                properties.setBoolean(SlimeProperties.PVP, true);

                var slimeWorld = slimePlugin.loadWorld(slimeLoader, map.getTemplateName(), true, properties).clone(map.getName());

                BukkitThreading.runTask(() -> {
                    ThrowingRunnable.of(
                            () -> slimePlugin.generateWorld(slimeWorld),
                            exception -> log.error("[SlimeWorldLoader] Failed to generate world {}: {}", map.getName(), exception.getMessage()));

                    log.info("[SlimeWorldLoader] World {} generated in {}ms", map.getName(), System.currentTimeMillis() - start);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            var world = Bukkit.getWorld(map.getName());

                            if (world != null) {
                                log.info("[SlimeWorldLoader] World {} fully loaded in {}ms", map.getName(), System.currentTimeMillis() - start);
                                map.setWorld(world);
                                future.complete(map);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(BukkitThreading.plugin, 2, 2);

                });
            } catch (Exception exception) {
                log.error("[SlimeWorldLoader] ERROR WHEN LOADING MAP", exception);
            }
        });

        return future;
    }

    @Override
    public void unload(SlimeMap map) {
        var world = Bukkit.getWorld(map.getName());

        if (world == null) return;

        world.getPlayers()
                .forEach(player -> BukkitThreading.runTask(() -> player.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation())));

        Bukkit.unloadWorld(world, false);
    }

    @Override
    public CompletableFuture<SlimeMap> delete(SlimeMap map) {
        return CompletableFuture
                .supplyAsync(() -> {
                    ThrowingRunnable.of(() -> slimeLoader.deleteWorld(map.getName())).run();
                    return map;
                })
                .whenCompleteAsync((slimeMap, throwable) -> {
                    if (throwable != null)
                        log.error("[SlimeWorldLoader] ERROR WHEN DELETE FILE: {}", throwable.getMessage());
                    else
                        log.info("[SlimeWorldLoader] DELETE COMPLETE FOR: {}", slimeMap.getName());
                });
    }

    @Override
    public CompletableFuture<SlimeMap> delete(SlimeMap map, Duration duration) {
        CompletableFuture<SlimeMap> future = new CompletableFuture<>();

        MultiThreading.schedule(() -> delete(map)
                .whenCompleteAsync((result, throwable) -> {
                    if (throwable != null)
                        future.completeExceptionally(throwable);
                    else
                        future.complete(result);
                }), duration.getSeconds(), TimeUnit.SECONDS);

        return future;
    }

}