package fr.heriamc.games.core.gui.game;

import fr.heriamc.api.game.GameState;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.pagination.HeriaPaginationMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.api.pool.GamePoolManager;
import fr.heriamc.games.engine.MiniGame;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class GameGui extends HeriaPaginationMenu<MiniGame> {

    private static final List<Integer> slots = List.of(
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34);

    public GameGui(Player player, GamePoolManager poolManager) {
        super(player, "Parties", 54, true, slots, poolManager::getAllGames);
    }

    @Override
    public void inventory(Inventory inventory) {
        setBorder(inventory, DyeColor.ORANGE.getWoolData());

        insertInteractItem(inventory, 48, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                .setName("§c→ Fermer le menu")
                .onClick(event -> getPlayer().closeInventory()));
    }

    @Override
    protected ItemBuilder item(MiniGame game, int i, int i1) {
        var state = game.getState();

        return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setName(state.getColor().getColor() + game.getFullName())
                .setSkullURL(state.getSkull().getURL())
                .setLoreWithList(
                        " ",
                        "§8» §7Type: §6" + game.getGameSize().getName().toUpperCase(),
                        "§8» §7État: " + chooseName(state),
                        " ",
                        "§8» §7Joueurs: §b" + game.getSize() + "§7/§b" + game.getGameSize().getMaxPlayer(),
                        "§8» §7Joueurs en vie: §a" + game.getAlivePlayersCount(),
                        "§8» §7Spectateurs: §f" + game.getSpectatorsCount(),
                        " ",
                        "§6§l❱ §eClique pour voir la liste des joueurs")
                .onClick(event -> HeriaBukkit.get().getMenuManager().open(new GamePlayerListGui(getPlayer(), game, this)));
    }

    private String chooseName(GameState state) {
        return switch (state) {
            case LOADING -> state.getColor().getColor() + "Chargement";
            case LOADING_IN_PROGRESS -> state.getColor().getColor() + "Chargement en cours...";
            case WAIT -> state.getColor().getColor() + "Attente";
            case ALWAYS_PLAYING -> state.getColor().getColor() + "Partie en continue";
            case STARTING -> state.getColor().getColor() + "Démarage";
            case IN_GAME -> state.getColor().getColor() + "En jeu";
            case END -> state.getColor().getColor() + "Fin";
        };
    }

}