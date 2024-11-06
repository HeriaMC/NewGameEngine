package fr.heriamc.games.engine.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

@UtilityClass
public class NameTag {

    public void setNameTag(Player player, String prefix, String suffix, int sortPriority) {
        var manager = player.getScoreboard();
        var priority = ((sortPriority < 10) ? "0" : "") + sortPriority;
        Team team = null;

        for (Team t : manager.getTeams()) {
            if (t.getPrefix().equals(prefix) && t.getSuffix().equals(suffix) && t.getName().startsWith(priority)) {
                team = t;
                break;
            }
        }

        while (team == null) {
            var tn = priority + UUID.randomUUID().toString().substring(30);
            if (manager.getTeam(tn) == null) {
                team = manager.registerNewTeam(tn);
                team.setPrefix(prefix);
                team.setSuffix(suffix);
            }
        }

        team.addEntry(player.getName());
    }

}