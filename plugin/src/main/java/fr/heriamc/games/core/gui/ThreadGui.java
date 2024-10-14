package fr.heriamc.games.core.gui;

import fr.heriamc.bukkit.menu.pagination.HeriaPaginationMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ThreadGui extends HeriaPaginationMenu<Thread> {

    private final static List<Integer> slots = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44);

    private final static int[] borders = new int[] { 45, 46, 47, 51, 52, 53 };

    public ThreadGui(Player player) {
        super(player, "§e§l» §6Threads", 54, true, slots, () -> new ArrayList<>(Thread.getAllStackTraces().keySet()));

        setLeftSlot(48);
        setRightSlot(50);
    }

    @Override
    public void inventory(Inventory inventory) {
        for (int i : borders)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.ORANGE.getData()).build());
        
        inventory.setItem(49, getInformationButton());
    }

    private ItemStack getInformationButton() {
        return new ItemBuilder(Material.BOOK)
                .setName("§e§l» §aInformations")
                .setLoreWithList(
                        "§7§l▏ §eActive Threads §7: §5" + Thread.getAllStackTraces().size(),
                        "§7§l▏ §eThread Pool Size §7: §d" + MultiThreading.getTotal(),
                        "§7§l▏ §eBoard Thread Pool Size §7: §d" + getBoardThreadCount(),
                        "§7§l▏ §eHeapMemory Usage §7: §2" + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024) / 1024 + " MB",
                        "§7§l▏ §eNonHeapMemory Usage §7: §2" + (ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed() / 1024) / 1024 + " MB",
                        "§7§l▏ §eThread Peak §7: §9" + ManagementFactory.getThreadMXBean().getPeakThreadCount()
                ).build();
    }

    @Override
    protected ItemBuilder item(Thread thread, int slot, int page) {
        return new ItemBuilder(Material.WOOL)
                .setDurability(chooseColor().apply(thread))
                .setName("§e§l» §a" + thread.getName())
                .setLoreWithList(
                        "§7§l▏ §eState §7: §b" + thread.getState(),
                        "§7§l▏ §ePriority §7: §c" + thread.getPriority(),
                        "§7§l▏ §eDeamon §7: §9" + thread.isDaemon(),
                        "§7§l▏ §eCPU Time §7: §6" + (ManagementFactory.getThreadMXBean().getThreadCpuTime(thread.threadId()) / 1000000) + " ms");
    }

    private Function<Thread, Byte> chooseColor() {
        return thread -> switch (thread.getState()) {
            case NEW -> DyeColor.WHITE.getData();
            case RUNNABLE -> DyeColor.LIME.getData();
            case BLOCKED -> DyeColor.RED.getData();
            case WAITING -> DyeColor.YELLOW.getData();
            case TIMED_WAITING -> DyeColor.ORANGE.getData();
            case TERMINATED -> DyeColor.BLACK.getData();
        };
    }

    private long getBoardThreadCount() {
        return Thread.getAllStackTraces().keySet().stream()
                .filter(thread -> thread.getName().startsWith("board-"))
                .count();
    }

}