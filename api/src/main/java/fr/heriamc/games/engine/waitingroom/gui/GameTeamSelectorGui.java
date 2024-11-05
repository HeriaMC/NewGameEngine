package fr.heriamc.games.engine.waitingroom.gui;

import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.Game;
import fr.heriamc.games.engine.player.GamePlayer;
import fr.heriamc.games.engine.team.GameTeam;
import fr.heriamc.games.engine.utils.item.GameSkull;
import fr.heriamc.games.engine.utils.gui.GameGui;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class GameTeamSelectorGui<M extends Game<G, T, ?>, G extends GamePlayer<T>, T extends GameTeam<G>> extends GameGui<M, G, T> {

    private static final int[] fill = new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25 };

    private final ItemBuilder randomItem;

    public GameTeamSelectorGui(M game, G gamePlayer, String name) {
        super(game, gamePlayer, name, 36, true);
        this.randomItem = getRandomTeamItem(game, gamePlayer);
    }

    @Override
    public void contents(Inventory inventory) {
        var playerTeam = gamePlayer.getTeam();

        setBorder(inventory, playerTeam != null ? playerTeam.getColor().getDyeColor().getData() : DyeColor.WHITE.getData());
        insertInteractItem(inventory, 31, randomItem);

        for (int i = 0; i < fill.length; i++) {
            if (i >= game.getTeams().size()) continue;

            final var team = game.getTeams().get(i);

            if (team != null)
                insertInteractItem(inventory, fill[i], getTeamButton(team));
        }
    }

    public ItemBuilder getTeamButton(T team) {
        var playerTeam = gamePlayer.getTeam();

        final var builder = new ItemBuilder(Material.WOOL, 1, team.getColor().getDyeColor().getWoolData())
                .setName(team.getColoredName() + (team.isMember(gamePlayer) ? " §a[Sélectionner]" : ""))
                .onClick(event -> {
                    if (playerTeam != null && playerTeam.equals(team)) return;

                    if (team.canJoin()) {
                        game.addPlayerToTeam(gamePlayer, team);
                        updateMenu();
                    }
                });

        for (var member : team.getMembers())
            builder.addLore("§7- " + member.getName());

        builder.addLore(" ");
        builder.addLore(team.isMember(gamePlayer)
                ? "§8■ §cVous êtes déjà dans cette équipe"
                : "§8■ §eCliquez pour rejoindre");

        return builder;
    }

    public ItemBuilder getRandomTeamItem(M game, G gamePlayer) {
        return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setName("§fAléatoire")
                .setCustomHeadData(GameSkull.DICE.getData())
                .onClick(event -> {
                    game.addPlayerToRandomTeam(gamePlayer);
                    updateMenu();
                });
    }

}