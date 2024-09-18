package fr.heriamc.games.engine.reward.core;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.reward.GameReward;
import fr.heriamc.games.engine.reward.RewardType;

public class HostReward extends GameReward {

    private final int amount;

    public HostReward(int amount) {
        super(RewardType.HERIA_HOSTS);
        this.amount = amount;
    }

    @Override
    public void reward(BaseGamePlayer gamePlayer) {
        var currentHosts = gamePlayer.getHeriaPlayer().getHosts();

        gamePlayer.getHeriaPlayer().setHosts(currentHosts + amount);
    }

}