package fr.heriamc.games.engine.utils.gui.player;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.gui.Gui;
import lombok.Getter;

@Getter
public abstract class PlayerGui<G extends BaseGamePlayer> extends Gui {

    private final G gamePlayer;

    public PlayerGui(G gamePlayer, String inventoryName, int rows) {
        super(inventoryName, rows);
        this.gamePlayer = gamePlayer;
    }

}