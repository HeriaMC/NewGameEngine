package fr.heriamc.games.core.manager;

import fr.heriamc.games.api.GuiManager;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import fr.heriamc.games.engine.utils.gui.Gui;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Getter
public class GameGuiManager implements GuiManager {

    private final ConcurrentMap<UUID, Gui> guis;

    public GameGuiManager(JavaPlugin plugin) {
        this.guis = new ConcurrentHashMap<>();
        MultiThreading.schedule(this::updateButtons, 1, 1, TimeUnit.SECONDS);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void updateButtons() {
        guis.values().stream().filter(Gui::isUpdatable).forEach(Gui::onUpdate);
    }

    @Override
    public Optional<Gui> getGui(UUID uuid) {
        return Optional.ofNullable(guis.get(uuid));
    }

    @Override
    public Optional<Gui> getGui(HumanEntity player) {
        return getGui(player.getUniqueId());
    }

    @Override
    public Optional<Gui> getGui(BaseGamePlayer gamePlayer) {
        return getGui(gamePlayer.getUuid());
    }

    @Override
    public void open(HumanEntity humanEntity, Gui gui) {
        guis.put(humanEntity.getUniqueId(), gui);
        getGui(humanEntity).ifPresent(menu -> menu.onOpen(humanEntity));
    }

    @Override
    public void open(Player player, Gui gui) {
        guis.put(player.getUniqueId(), gui);
        getGui(player).ifPresent(menu -> menu.onOpen(player));
    }

    @Override
    public void open(BaseGamePlayer gamePlayer, Gui gui) {
        open(gamePlayer.getPlayer(), gui);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null) return;


        getGui(event.getWhoClicked())
                .filter(gui -> event.getInventory().equals(gui.getInventory()))
                .ifPresent(gui -> {
                    if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        event.setCancelled(true);
                        return;
                    }

                    if (!event.getClickedInventory().equals(gui.getInventory())) return;

                    if (itemStack.getType() == Material.SKULL_ITEM)
                        gui.getButtons().entrySet().stream()
                                .filter(entry -> entry.getValue().getItemStack().hasItemMeta()
                                        && entry.getValue().getItemStack().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()))
                                .findFirst().ifPresent(entry -> entry.getValue().getConsumer().accept(event));
                    else
                        gui.getButtons().entrySet().stream()
                                .filter(entry -> entry.getValue().getItemStack().equals(itemStack))
                                .findFirst().ifPresent(entry -> entry.getValue().getConsumer().accept(event));

                    event.setCancelled(true);
                });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrag(InventoryDragEvent event) {
        getGui(event.getWhoClicked())
                .filter(gui -> event.getInventory().equals(gui.getInventory()))
                .ifPresent(gui -> event.setCancelled(true));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(InventoryMoveItemEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player)
            getGui(player).filter(gui -> event.getInventory().equals(gui.getInventory())
                            && gui.getCloseConsumer() != null)
                    .ifPresent(gui -> gui.getCloseConsumer().accept(event));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        guis.remove(player.getUniqueId());
    }

}