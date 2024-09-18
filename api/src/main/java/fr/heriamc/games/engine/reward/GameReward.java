package fr.heriamc.games.engine.reward;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public abstract class GameReward implements Reward {

    private final RewardType type;

    @Override
    public void reward(Collection<BaseGamePlayer> gamePlayers) {
        gamePlayers.forEach(this::reward);
    }

}