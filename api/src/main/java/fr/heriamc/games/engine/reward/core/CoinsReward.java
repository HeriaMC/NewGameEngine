package fr.heriamc.games.engine.reward.core;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.reward.GameReward;
import fr.heriamc.games.engine.reward.RewardType;

public class CoinsReward extends GameReward {

    private final float amount;

    public CoinsReward(float amount) {
        super(RewardType.HERIA_COINS);
        this.amount = amount;
    }

    @Override
    public void reward(BaseGamePlayer gamePlayer) {
        var currentCoins = gamePlayer.getHeriaPlayer().getCoins();

        gamePlayer.getHeriaPlayer().setCoins(currentCoins + amount);
    }

}