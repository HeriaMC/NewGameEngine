package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Getter
@Setter
public abstract class Gui {

    private final String inventoryName;
    private final int rows;
    private final ConcurrentMap<Integer, GuiButton> buttons;

    private Inventory inventory;
    private Consumer<InventoryCloseEvent> closeConsumer;
    private boolean updatable;

    public Gui(String inventoryName, int rows, boolean updatable) {
        this.inventoryName = inventoryName;
        this.rows = rows;
        this.buttons = new ConcurrentHashMap<>();
        this.inventory = Bukkit.createInventory(null, rows * 9, inventoryName);
        this.updatable = updatable;
        this.closeConsumer = this::onClose;
        defaultLoad();
    }

    public Gui(String inventoryName, int rows) {
        this(inventoryName, rows, false);
    }

    public abstract void setup();

    public void onUpdate() {}

    public void onClose(InventoryCloseEvent event) {
        GameApi.getInstance().getGuiManager().getGuis().remove(event.getPlayer().getUniqueId());
    }

    public void onOpen(HumanEntity player) {
        setup();
        open(player);
    }

    private void open(HumanEntity player) {
        player.openInventory(inventory);
    }

    public void close(HumanEntity player) {
        player.closeInventory();
    }

    public void close(BaseGamePlayer gamePlayer) {
        close(gamePlayer.getPlayer());
    }

    public void setItem(int slot, GuiButton button) {
        buttons.put(slot, button);
        inventory.setItem(slot, button.getItemStack());
        //update();
    }

    public void setItem(int slot, ItemStack itemStack) {
        setItem(slot, new GuiButton(itemStack));
    }

    public void setItems(int[] slots, GuiButton button) {
        Arrays.stream(slots).forEach(slot -> setItem(slot, button));
    }

    public void setItems(int[] slots, ItemStack itemStack) {
        setItems(slots, new GuiButton(itemStack));
    }

    public void setItems(List<Integer> slots, GuiButton button) {
        slots.forEach(slot -> setItem(slot, button));
    }

    public void setItems(List<Integer> slots, ItemStack itemStack) {
        setItems(slots, new GuiButton(itemStack));
    }

    public void setHorizontalLine(int from, int to, GuiButton button) {
        IntStream.rangeClosed(from, to)
                .forEach(slot -> setItem(slot, button));
    }

    public void setHorizontalLine(int from, int to, ItemStack itemStack) {
        setHorizontalLine(from, to, new GuiButton(itemStack));
    }

    public void setVerticalLine(int from, int to, GuiButton button) {
        IntStream.iterate(from, slot -> slot + 9)
                .limit((to - from) / 9 + 1)
                .forEach(slot -> setItem(slot, button));
    }

    public void setVerticalLine(int from, int to, ItemStack itemStack) {
        setVerticalLine(from, to, new GuiButton(itemStack));
    }

    public void addItem(GuiButton button) {
        setItem(inventory.firstEmpty(), button);
    }

    public void addItem(ItemStack itemStack) {
        addItem(new GuiButton(itemStack));
    }

    public void fillAllInventory(GuiButton button) {
        IntStream.range(0, rows * 9)
                .filter(slot -> getItem(slot) != null)
                .forEach(slot -> setItem(slot, button));
    }

    public void fillAllInventory(ItemStack itemStack) {
        fillAllInventory(new GuiButton(itemStack));
    }

    public int[] getBorders() {
        int size = rows * 9;

        return IntStream.range(0, size)
                .filter(slot -> size < 27 || slot < 9 || slot % 9 == 0 || (slot - 8) % 9 == 0 || slot > size - 9).parallel()
                .toArray();
    }

    public void removeItem(int slot) {
        buttons.remove(slot);
        inventory.remove(inventory.getItem(slot));
    }

    public void clear() {
        buttons.keySet().forEach(this::removeItem);
    }

    private void defaultLoad() {
        buttons.forEach((slot, item) -> getInventory().setItem(slot, item.getItemStack()));
    }

    public void refresh() {
        clear();
        setup();
    }

    public GuiButton getItem(int slot) {
        return buttons.get(slot);
    }

    public int getSize() {
        return rows * 9;
    }

}