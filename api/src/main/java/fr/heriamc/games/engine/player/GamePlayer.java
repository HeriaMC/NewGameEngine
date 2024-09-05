package fr.heriamc.games.engine.player;

import fr.heriamc.games.engine.team.GameTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
@Setter
public abstract class GamePlayer<T extends GameTeam<?>> extends SimpleGamePlayer {

    private T team;

    public GamePlayer(UUID uuid, int kills, int deaths, T team, boolean spectator) {
        super(uuid, kills, deaths, spectator);
        this.team = team;
    }

    public GamePlayer(UUID uuid, int kills, int deaths, boolean spectator) {
        this(uuid, kills, deaths, null, spectator);
    }

    public <G extends GamePlayer<?>> void setTeam(GameTeam<G> team) {
        this.team = (T) team;
    }

    public boolean hasTeam() {
        return team != null;
    }

    public boolean hasNoTeam() {
        return team == null;
    }

}