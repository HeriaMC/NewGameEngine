package fr.heriamc.games.core.gui.game;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.menu.pagination.HeriaPaginationMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.concurrent.BukkitThreading;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class GamePlayerListGui extends HeriaPaginationMenu<BaseGamePlayer> {

    private static final List<Integer> slots = List.of(
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34);

    private final HeriaMenu beforeMenu;

    public GamePlayerListGui(Player player, MiniGame game, HeriaMenu beforeMenu) {
        super(player, game.getFullName(), 54, true, slots, () -> game.getPlayers().values().stream().toList());
        this.beforeMenu = beforeMenu;
    }

    @Override
    public void inventory(Inventory inventory) {
        setBorder(inventory, DyeColor.ORANGE.getWoolData());

        insertInteractItem(inventory, 48, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                .setName("§c→ Fermer le menu")
                .onClick(event -> HeriaBukkit.get().getMenuManager().open(beforeMenu)));
    }

    @Override
    protected ItemBuilder item(BaseGamePlayer gamePlayer, int i, int i1) {
        return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setName("§e" + gamePlayer.getName())
                .setSkullOwner(gamePlayer.getName())
                .setLoreWithList(
                        " ",
                        "§8» §7En vie: " + (gamePlayer.isAlive() ? "§a✓" : "§c✘"),
                        "§8» §7Spectateur: " + (gamePlayer.isSpectator() ? "§a✓" : "§c✘"),
                        " ",
                        "§6§l❱ §eClique pour ce téléporter")
                .onClick(event -> {
                    // SECURITY CHECK
                    if (getPlayer().getUniqueId().equals(gamePlayer.getUuid())) return;

                    if (HeriaAPI.get().getPlayerManager().get(getPlayer().getUniqueId()).getRank().getPower() >= HeriaRank.MOD.getPower())
                        BukkitThreading.runTask(() -> getPlayer().teleport(gamePlayer.getLocation()));
                });
    }

}