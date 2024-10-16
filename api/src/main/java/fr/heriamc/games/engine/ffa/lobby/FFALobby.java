package fr.heriamc.games.engine.ffa.lobby;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.ffa.player.FFAGamePlayer;

public interface FFALobby {

    void onJoin(MiniGame game, FFAGamePlayer gamePlayer);

    void onSetup(MiniGame game, FFAGamePlayer gamePlayer);

    void onPlay(MiniGame game, FFAGamePlayer gamePlayer);

}