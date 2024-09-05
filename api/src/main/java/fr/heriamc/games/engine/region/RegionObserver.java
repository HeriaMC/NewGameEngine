package fr.heriamc.games.engine.region;

import fr.heriamc.games.engine.player.BaseGamePlayer;

public interface RegionObserver<G extends BaseGamePlayer> {

    void onEnter(G gamePlayer);

    void onExit(G gamePlayer);

}