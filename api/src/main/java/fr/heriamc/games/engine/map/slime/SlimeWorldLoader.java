package fr.heriamc.games.engine.map.slime;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.plugin.SWMPlugin;
import fr.heriamc.games.engine.map.GameMapLoader;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import fr.heriamc.games.engine.utils.func.ThrowingRunnable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@AllArgsConstructor
public class SlimeWorldLoader implements GameMapLoader<SlimeMap> {

    private final SlimePlugin slimePlugin;
    private final SlimeLoader slimeLoader;

    public SlimeWorldLoader(String slimeLoaderName) {
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
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

        SWMPlugin instance = SWMPlugin.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

            try {
                final long start = System.currentTimeMillis();
                final SlimeLoader loader = slimePlugin.getLoader("file");

                final SlimePropertyMap properties = new SlimePropertyMap();
                properties.setString(SlimeProperties.DIFFICULTY, "normal");
                properties.setBoolean(SlimeProperties.PVP, true);

                final SlimeWorld slimeWorld = slimePlugin.loadWorld(loader, map.getTemplateName(), true, properties).clone(map.getName());
                Bukkit.getScheduler().runTask(instance, () -> {
                    try {
                        slimePlugin.generateWorld(slimeWorld);
                    } catch (IllegalArgumentException ex) {
                        Bukkit.getLogger().warning("[Core - World] " + ChatColor.RED + "Failed to generate world " + map.getName() + ": " + ex.getMessage() + ".");
                        return;
                    }

                    long ms = (System.currentTimeMillis() - start);
                    Bukkit.getLogger().warning("[Core - World] " + ChatColor.GREEN + "World " + ChatColor.YELLOW + map.getName()
                            + ChatColor.GREEN + " loaded and generated in " + ms + "ms!");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            final World world = Bukkit.getWorld(map.getName());
                            if (world != null) {
                                map.setWorld(world);
                                future.complete(map);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(instance, 2, 2);
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        return future;
    }


    @Override
    public void unload(SlimeMap map) {
        World world = Bukkit.getWorld(map.getName());

        world.getPlayers()
                .forEach(player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));

        Optional.ofNullable(map.getWorld())
                .ifPresent(slimeWorld -> Bukkit.unloadWorld(world, false));
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
                        log.error("ERROR WHEN DELETE FILE: {}", throwable.getMessage());
                    else
                        log.info("DELETE COMPLETE FOR: {}", slimeMap.getName());
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