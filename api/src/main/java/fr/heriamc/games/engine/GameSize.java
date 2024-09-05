package fr.heriamc.games.engine;

import fr.heriamc.games.engine.utils.GameSizeTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameSize {

    private String name;
    private int minPlayer, maxPlayer, teamNeeded, teamMaxPlayer;

    public GameSize(GameSizeTemplate template) {
        this.name = template.getName();
        this.minPlayer = template.getMinPlayer();
        this.maxPlayer = template.getMaxPlayer();
        this.teamNeeded = template.getTeamNeeded();
        this.teamMaxPlayer = template.getTeamMaxPlayer();
    }

    public int calculateMapCapacity() {
        return maxPlayer == 999 ? 30 : maxPlayer;
    }

}