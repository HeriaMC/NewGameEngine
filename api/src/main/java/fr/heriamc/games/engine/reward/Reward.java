package fr.heriamc.games.engine.reward;

import fr.heriamc.games.engine.player.BaseGamePlayer;

import java.util.Collection;

public interface Reward {

    RewardType getType();

    void reward(BaseGamePlayer gamePlayer);
    void reward(Collection<BaseGamePlayer> gamePlayers);

}