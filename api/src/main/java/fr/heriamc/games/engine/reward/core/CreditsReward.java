package fr.heriamc.games.engine.reward.core;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.reward.GameReward;
import fr.heriamc.games.engine.reward.RewardType;

public class CreditsReward extends GameReward {

    private final int amount;

    public CreditsReward(int amount) {
        super(RewardType.HERIA_CREDITS);
        this.amount = amount;
    }

    @Override
    public void reward(BaseGamePlayer gamePlayer) {
        int currentCredits = (int) gamePlayer.getHeriaPlayer().getCredits();

        gamePlayer.getHeriaPlayer().setCredits(currentCredits + amount);
    }

}